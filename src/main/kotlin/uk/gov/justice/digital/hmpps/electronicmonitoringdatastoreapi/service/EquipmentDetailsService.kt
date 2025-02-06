package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.EquipmentDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.EquipmentDetailsRepository

@Service
class EquipmentDetailsService(
  @Autowired val equipmentDetailsRepository: EquipmentDetailsRepository,
) {
  fun getEquipmentDetails(orderId: String, role: AthenaRole): List<EquipmentDetails> {
    val result = equipmentDetailsRepository.getEquipmentDetails(orderId, role)

    return result.map { item -> EquipmentDetails(item) }
  }
}
