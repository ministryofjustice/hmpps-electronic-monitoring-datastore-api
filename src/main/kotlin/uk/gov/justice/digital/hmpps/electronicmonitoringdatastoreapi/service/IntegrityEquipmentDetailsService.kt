package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.IntegrityEquipmentDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.IntegrityEquipmentDetailsRepository

@Service
class IntegrityEquipmentDetailsService(
  val integrityEquipmentDetailsRepository: IntegrityEquipmentDetailsRepository,
) {
  fun getEquipmentDetails(legacySubjectId: String, restricted: Boolean): List<IntegrityEquipmentDetails> {
    val result = integrityEquipmentDetailsRepository.findByLegacySubjectIdAndRestricted(legacySubjectId, restricted)

    return result.map { item -> IntegrityEquipmentDetails(item) }
  }
}
