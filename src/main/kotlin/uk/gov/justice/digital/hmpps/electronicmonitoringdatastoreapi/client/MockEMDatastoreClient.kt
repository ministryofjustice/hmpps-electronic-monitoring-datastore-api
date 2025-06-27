package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import java.io.File
import kotlin.io.readText

@Component
@Profile("mocking")
class MockEMDatastoreClient : EmDatastoreClientInterface {
  private companion object {
    private const val MOCKS_RESOURCE_PATH = "./src/main/resources/mockAthenaResponses"

    private const val MOCK_QUERY_EXECUTION_ID = "mock-query-execution-id"

    private val log = LoggerFactory.getLogger(this::class.java)

    private var responses: MutableMap<String, String> = mutableMapOf<String, String>()

    private fun loadResponses() {
      val scenarios = File(MOCKS_RESOURCE_PATH).listFiles()?.map { it.nameWithoutExtension }

      if (scenarios!!.isEmpty()) {
        return
      }

      scenarios.forEach { scenario ->
        val rawQuery = File("$MOCKS_RESOURCE_PATH/$scenario/query.sql").readText(Charsets.UTF_8)
        val query = stripWhitespace(rawQuery)

        val response = File("$MOCKS_RESOURCE_PATH/$scenario/response.json").readText(Charsets.UTF_8)

        responses[query] = response
      }
    }

    private fun stripWhitespace(value: String): String = value.lines()
      .joinToString(" ") { line -> line.trim() }
      .trimIndent()
  }

  override fun getQueryExecutionId(athenaQuery: AthenaQuery, restricted: Boolean): String = getQueryExecutionId(athenaQuery, null)

  override fun getQueryExecutionId(athenaQuery: AthenaQuery, role: AthenaRole?): String {
    if (athenaQuery.queryString == "THROW ERROR") {
      throw IllegalArgumentException("I threw an error")
    }

    if (responses.isEmpty()) {
      loadResponses()
    }

    val query = stripWhitespace(athenaQuery.queryString)

    val athenaResponse = responses[query]?.trimIndent()
    if (athenaResponse == null) {
      log.info(
        """
          No response defined for query
          -------------
          ${athenaQuery.queryString}
          -------------
        """.trimIndent(),
      )
    }

    return MOCK_QUERY_EXECUTION_ID
  }

  override fun getQueryResult(queryExecutionId: String, restricted: Boolean): ResultSet = getQueryResult(queryExecutionId, null)

  override fun getQueryResult(queryExecutionId: String, role: AthenaRole?): ResultSet {
    if (queryExecutionId == "THROW ERROR") {
      throw IllegalArgumentException("I threw an error")
    }

    if (queryExecutionId != MOCK_QUERY_EXECUTION_ID) {
      log.info(
        """
          No response defined for query execution ID $queryExecutionId
        """.trimIndent(),
      )
    }

    val athenaResponse = File("$MOCKS_RESOURCE_PATH/successfulGetSearchResults/response.json").readText(Charsets.UTF_8).trimIndent()

    return AthenaHelper.resultSetFromJson(athenaResponse)
  }

  override fun getQueryResult(athenaQuery: AthenaQuery, restricted: Boolean): ResultSet = getQueryResult(athenaQuery, null)

  override fun getQueryResult(athenaQuery: AthenaQuery, role: AthenaRole?): ResultSet {
    if (athenaQuery.queryString == "THROW ERROR") {
      throw IllegalArgumentException("I threw an error")
    }

    if (responses.isEmpty()) {
      loadResponses()
    }

    val query = stripWhitespace("${athenaQuery.queryString}${athenaQuery.parameters.joinToString(",")}")

    val athenaResponse = responses[query]?.trimIndent()
    if (athenaResponse == null) {
      log.info(
        """
          No response defined for query
          -------------
          $query
          -------------
        """.trimIndent(),
      )
    }

    return AthenaHelper.resultSetFromJson(athenaResponse!!)
  }
}
