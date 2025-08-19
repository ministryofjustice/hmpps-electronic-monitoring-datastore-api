package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AmEquipmentDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring.AmEquipmentDetailsRepository

@Service
class AmEquipmentDetailsService(
  val amEquipmentDetailsRepository: AmEquipmentDetailsRepository,
) {
  fun getEquipmentDetails(legacySubjectId: String): List<AmEquipmentDetails> {
    val result = amEquipmentDetailsRepository.findByLegacySubjectId(legacySubjectId)

    return result.map { item -> AmEquipmentDetails(item) }
  }
}
