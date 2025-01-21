package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.mocks

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
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

  override fun getQueryResult(athenaQuery: AthenaQuery, role: AthenaRole?): ResultSet {
    if (athenaQuery.queryString == "THROW ERROR") {
      throw IllegalArgumentException("I threw an error")
    }

    val athenaResponse = getNextResponse()
    return AthenaHelper.Companion.resultSetFromJson(athenaResponse)
  }
}
