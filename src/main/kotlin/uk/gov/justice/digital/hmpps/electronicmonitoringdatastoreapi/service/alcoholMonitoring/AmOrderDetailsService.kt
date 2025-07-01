package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmOrderDetailsRepository
import kotlin.String

@Service
class AmOrderDetailsService(
  val orderDetailsRepository: AmOrderDetailsRepository,
) {

  fun getOrderDetails(legacySubjectId: String): AmOrderDetails {
    val orderDetails = orderDetailsRepository.getOrderDetails(legacySubjectId)

    return AmOrderDetails(orderDetails)
  }

  fun getQueryExecutionId(criteria: OrderSearchCriteria, restricted: Boolean): String = orderDetailsRepository.searchOrders(criteria, restricted)

  fun getSearchResults(queryExecutionId: String, restricted: Boolean): List<AmOrderDetails> {
    val results = orderDetailsRepository.getSearchResults(queryExecutionId, restricted)
    return results.map { result -> AmOrderDetails(result) }
  }
}
