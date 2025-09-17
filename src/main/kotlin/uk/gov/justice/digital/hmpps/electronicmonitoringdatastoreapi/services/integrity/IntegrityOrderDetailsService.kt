package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.integrity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.integrity.IntegrityOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.integrity.IntegrityOrderDetailsRepository

@Service
class IntegrityOrderDetailsService(
  @field:Autowired val integrityOrderDetailsRepository: IntegrityOrderDetailsRepository,
) {
  fun getOrderDetails(legacySubjectId: String, restricted: Boolean): IntegrityOrderDetails {
    val orderDetailsDTO = integrityOrderDetailsRepository.findByLegacySubjectIdAndRestricted(legacySubjectId, restricted)
    return IntegrityOrderDetails(orderDetailsDTO)
  }

  fun getQueryExecutionId(criteria: OrderSearchCriteria, restricted: Boolean): String = integrityOrderDetailsRepository.searchOrders(criteria, restricted)

  fun getSearchResults(queryExecutionId: String, restricted: Boolean): List<IntegrityOrderDetails> {
    val results = integrityOrderDetailsRepository.getSearchResults(queryExecutionId, restricted)
    return results.map { result -> IntegrityOrderDetails(result) }
  }
}
