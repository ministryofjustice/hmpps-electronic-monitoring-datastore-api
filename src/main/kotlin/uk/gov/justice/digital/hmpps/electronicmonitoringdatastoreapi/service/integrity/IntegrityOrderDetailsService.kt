package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityOrderDetailsRepository

@Service
class IntegrityOrderDetailsService(
  @field:Autowired val integrityOrderDetailsRepository: IntegrityOrderDetailsRepository,
) {
  fun getOrderDetails(legacySubjectId: String, restricted: Boolean): IntegrityOrderDetails {
    val orderDetailsDTO = integrityOrderDetailsRepository.getOrderDetails(legacySubjectId, restricted)
    return IntegrityOrderDetails(orderDetailsDTO)
  }
}
