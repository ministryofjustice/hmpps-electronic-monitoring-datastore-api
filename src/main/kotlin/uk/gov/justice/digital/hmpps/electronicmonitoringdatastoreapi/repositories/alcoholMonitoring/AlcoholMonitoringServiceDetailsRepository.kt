package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AlcoholMonitoringServicesQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.alcoholMonitoring.AthenaAlcoholMonitoringServiceDetailsDTO

@Service
class AlcoholMonitoringServiceDetailsRepository(
  val athenaClient: EmDatastoreClientInterface,
) {
  fun findByLegacySubjectId(legacySubjectId: String): List<AthenaAlcoholMonitoringServiceDetailsDTO> {
    val amServicesQuery = AlcoholMonitoringServicesQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(amServicesQuery)

    return AthenaHelper.mapTo<AthenaAlcoholMonitoringServiceDetailsDTO>(athenaResponse)
  }
}
