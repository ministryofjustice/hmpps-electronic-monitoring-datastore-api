package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.integrity

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityEquipmentDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.integrity.AthenaIntegrityEquipmentDetailsDTO

@Service
class IntegrityEquipmentDetailsRepository(
  val athenaClient: EmDatastoreClientInterface,
) {
  fun getEquipmentDetails(legacySubjectId: String, restricted: Boolean): List<AthenaIntegrityEquipmentDetailsDTO> {
    val equipmentDetailsQuery = IntegrityEquipmentDetailsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(equipmentDetailsQuery, restricted)

    return AthenaHelper.mapTo<AthenaIntegrityEquipmentDetailsDTO>(athenaResponse)
  }
}
