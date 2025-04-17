package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityEquipmentDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityEquipmentDetailsRepository

@Service
class IntegrityEquipmentDetailsService(
  @Autowired val integrityEquipmentDetailsRepository: IntegrityEquipmentDetailsRepository,
) {
  fun getEquipmentDetails(legacySubjectId: String, role: AthenaRole): List<IntegrityEquipmentDetails> {
    val result = integrityEquipmentDetailsRepository.getEquipmentDetails(legacySubjectId, role)

    return result.map { item -> IntegrityEquipmentDetails(item) }
  }
}
