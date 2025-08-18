package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AmServicesQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.models.alcoholMonitoring.AthenaAmServiceDetailsDTO

@Service
class AmServiceDetailsRepository(
  val athenaClient: EmDatastoreClientInterface,
) {
  fun getServiceDetails(legacySubjectId: String): List<AthenaAmServiceDetailsDTO> {
    val amServicesQuery = AmServicesQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(amServicesQuery)

    return AthenaHelper.mapTo<AthenaAmServiceDetailsDTO>(athenaResponse)
  }
}
