package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import java.io.File

@Component
@Profile("integration")
class MockEmDatastoreClient : EmDatastoreClientInterface {

  @Value($$"${services.athena.database}")
  private val databaseName: String = "test_database"

  companion object {
    var responses: MutableList<String> = mutableListOf<String>()

    fun addResponseFile(responseFilename: String) {
      this.responses.add(responseFilename)
    }

    fun getNextResponse(): String {
      val responseFilename = "./src/test/resources/athenaResponses/${this.responses.removeFirst()}.json"
      return File(responseFilename).readText(Charsets.UTF_8)
    }

    fun reset() {
      this.responses.clear()
    }
  }

  override fun getQueryExecutionId(athenaQuery: SqlQueryBuilder, restricted: Boolean): String {
    val query = athenaQuery.build(databaseName)

    if (query.queryString == "THROW ERROR") {
      throw IllegalArgumentException("I threw an error")
    }

    return "query-execution-id"
  }

  override fun getQueryResult(athenaQuery: SqlQueryBuilder, restricted: Boolean): ResultSet {
    val query = athenaQuery.build(databaseName)

    if (query.queryString == "THROW ERROR") {
      throw IllegalArgumentException("I threw an error")
    }

    val athenaResponse = getNextResponse()
    return AthenaHelper.Companion.resultSetFromJson(athenaResponse)
  }

  override fun getQueryResult(queryExecutionId: String, restricted: Boolean): ResultSet {
    if (queryExecutionId == "THROW ERROR") {
      throw IllegalArgumentException("I threw an error")
    }

    val athenaResponse = getNextResponse()
    return AthenaHelper.Companion.resultSetFromJson(athenaResponse)
  }
}
