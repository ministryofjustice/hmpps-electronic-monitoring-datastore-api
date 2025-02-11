package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.MockEMDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SuspensionOfVisitsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSuspensionOfVisitsDTO

@Service
class SuspensionOfVisitsRepository(
  @Autowired var athenaClient: EmDatastoreClientInterface,
  @Value("\${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {

  init {
    athenaClient = MockEMDatastoreClient()
  }

  fun getSuspensionOfVisits(orderId: String, role: AthenaRole): List<AthenaSuspensionOfVisitsDTO> {
    val suspensionOfVisitsQuery = SuspensionOfVisitsQueryBuilder(athenaDatabase)
//      .withLegacySubjectId(orderId)
      .withLegacySubjectId("1401253")
      .build()

    val athenaResponse = athenaClient.getQueryResult(suspensionOfVisitsQuery, role)

    return AthenaHelper.Companion.mapTo<AthenaSuspensionOfVisitsDTO>(athenaResponse)
  }
}
