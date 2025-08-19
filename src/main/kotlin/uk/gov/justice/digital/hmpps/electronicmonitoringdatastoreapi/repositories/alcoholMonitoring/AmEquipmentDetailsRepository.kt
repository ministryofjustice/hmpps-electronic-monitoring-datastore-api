package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AmEquipmentDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.alcoholMonitoring.AthenaAmEquipmentDetailsDTO

@Service
class AmEquipmentDetailsRepository(
  val athenaClient: EmDatastoreClientInterface,
) {
  fun findByLegacySubjectId(legacySubjectId: String): List<AthenaAmEquipmentDetailsDTO> {
    val amEquipmentDetailsQuery = AmEquipmentDetailsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(amEquipmentDetailsQuery)

    return AthenaHelper.mapTo<AthenaAmEquipmentDetailsDTO>(athenaResponse)
  }
}
