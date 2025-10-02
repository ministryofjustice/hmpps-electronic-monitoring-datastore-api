package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.AlcoholMonitoringEquipmentDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.AlcoholMonitoringEquipmentDetailsRepository

@Service
class AlcoholMonitoringEquipmentDetailsService(
  val alcoholMonitoringEquipmentDetailsRepository: AlcoholMonitoringEquipmentDetailsRepository,
) {
  fun getEquipmentDetails(legacySubjectId: String): List<AlcoholMonitoringEquipmentDetails> {
    val result = alcoholMonitoringEquipmentDetailsRepository.findByLegacySubjectId(legacySubjectId)

    return result.map { item -> AlcoholMonitoringEquipmentDetails(item) }
  }
}
