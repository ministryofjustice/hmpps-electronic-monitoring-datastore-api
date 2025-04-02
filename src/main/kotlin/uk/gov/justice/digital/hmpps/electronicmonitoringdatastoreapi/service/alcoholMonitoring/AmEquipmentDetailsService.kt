package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmEquipmentDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmEquipmentDetailsRepository

@Service
class AmEquipmentDetailsService(
  @Autowired val amEquipmentDetailsRepository: AmEquipmentDetailsRepository,
) {
  fun getEquipmentDetails(legacySubjectId: String, role: AthenaRole): List<AmEquipmentDetails> {
    val result = amEquipmentDetailsRepository.getEquipmentDetails(legacySubjectId, role)

    return result.map { item -> AmEquipmentDetails(item) }
  }
}
