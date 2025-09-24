package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityEquipmentDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityEquipmentDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper

@Service
class IntegrityEquipmentDetailsRepository(
  athenaClient: EmDatastoreClient,
) : AthenaRepository<AthenaIntegrityEquipmentDetailsDTO>(athenaClient) {
  override fun mapTo(results: ResultSet): List<AthenaIntegrityEquipmentDetailsDTO> = AthenaHelper.mapTo(results)

  fun findByLegacySubjectIdAndRestricted(legacySubjectId: String, restricted: Boolean): List<AthenaIntegrityEquipmentDetailsDTO> = this.executeQuery(
    AthenaIntegrityEquipmentDetailsQueryBuilder().withLegacySubjectId(legacySubjectId),
    restricted,
  )
}
