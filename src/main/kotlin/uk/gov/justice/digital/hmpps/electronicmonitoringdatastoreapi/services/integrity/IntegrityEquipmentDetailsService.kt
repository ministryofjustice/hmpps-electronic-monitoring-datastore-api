package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.integrity

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.integrity.IntegrityEquipmentDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.integrity.IntegrityEquipmentDetailsRepository

@Service
class IntegrityEquipmentDetailsService(
  val integrityEquipmentDetailsRepository: IntegrityEquipmentDetailsRepository,
) {
  fun getEquipmentDetails(legacySubjectId: String, restricted: Boolean): List<IntegrityEquipmentDetails> {
    val result = integrityEquipmentDetailsRepository.getEquipmentDetails(legacySubjectId, restricted)

    return result.map { item -> IntegrityEquipmentDetails(item) }
  }
}
