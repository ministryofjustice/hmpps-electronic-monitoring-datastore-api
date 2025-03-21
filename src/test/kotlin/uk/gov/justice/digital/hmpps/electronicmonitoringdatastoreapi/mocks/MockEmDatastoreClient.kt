package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.mocks

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import java.io.File

@Component
@Profile("integration")
class MockEmDatastoreClient : EmDatastoreClientInterface {
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

  override fun getQueryExecutionId(athenaQuery: AthenaQuery, allowSpecials: Boolean?): String {
    if (athenaQuery.queryString == "THROW ERROR") {
      throw IllegalArgumentException("I threw an error")
    }

    return "query-execution-id"
  }

  override fun getQueryResult(athenaQuery: AthenaQuery, allowSpecials: Boolean?): ResultSet {
    if (athenaQuery.queryString == "THROW ERROR") {
      throw IllegalArgumentException("I threw an error")
    }

    val athenaResponse = getNextResponse()
    return AthenaHelper.Companion.resultSetFromJson(athenaResponse)
  }

  override fun getQueryResult(queryExecutionId: String, allowSpecials: Boolean?): ResultSet {
    if (queryExecutionId == "THROW ERROR") {
      throw IllegalArgumentException("I threw an error")
    }

    val athenaResponse = getNextResponse()
    return AthenaHelper.Companion.resultSetFromJson(athenaResponse)
  }
}
