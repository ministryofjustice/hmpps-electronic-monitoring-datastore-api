package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
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

  fun getQueryExecutionId(criteria: OrderSearchCriteria, restricted: Boolean): String = integrityOrderDetailsRepository.searchOrders(criteria, restricted)

  fun getSearchResults(queryExecutionId: String, restricted: Boolean): List<IntegrityOrderDetails> {
    val results = integrityOrderDetailsRepository.getSearchResults(queryExecutionId, restricted)
    return results.map { result -> IntegrityOrderDetails(result) }
  }
}
