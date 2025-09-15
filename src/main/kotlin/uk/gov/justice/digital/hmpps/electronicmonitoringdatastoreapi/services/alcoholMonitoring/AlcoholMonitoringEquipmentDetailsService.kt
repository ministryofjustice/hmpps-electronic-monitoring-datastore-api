package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AlcoholMonitoringEquipmentDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring.AlcoholMonitoringEquipmentDetailsRepository

@Service
class AlcoholMonitoringEquipmentDetailsService(
  val alcoholMonitoringEquipmentDetailsRepository: AlcoholMonitoringEquipmentDetailsRepository,
) {
  fun getEquipmentDetails(legacySubjectId: String): List<AlcoholMonitoringEquipmentDetails> {
    val result = alcoholMonitoringEquipmentDetailsRepository.findByLegacySubjectId(legacySubjectId)

    return result.map { item -> AlcoholMonitoringEquipmentDetails(item) }
  }
}
