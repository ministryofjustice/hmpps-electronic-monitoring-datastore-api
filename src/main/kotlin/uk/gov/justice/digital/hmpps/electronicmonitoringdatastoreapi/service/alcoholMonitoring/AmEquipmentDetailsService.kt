package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmEquipmentDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmEquipmentDetailsRepository

@Service
class AmEquipmentDetailsService(
  val amEquipmentDetailsRepository: AmEquipmentDetailsRepository,
) {
  fun getEquipmentDetails(legacySubjectId: String): List<AmEquipmentDetails> {
    val result = amEquipmentDetailsRepository.getEquipmentDetails(legacySubjectId)

    return result.map { item -> AmEquipmentDetails(item) }
  }
}
