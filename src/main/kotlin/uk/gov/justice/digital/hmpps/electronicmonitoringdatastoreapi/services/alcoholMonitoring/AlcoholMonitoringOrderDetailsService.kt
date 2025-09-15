package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AlcoholMonitoringOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring.AlcoholMonitoringOrderDetailsRepository
import kotlin.String

@Service
class AlcoholMonitoringOrderDetailsService(
  val orderDetailsRepository: AlcoholMonitoringOrderDetailsRepository,
) {

  fun getOrderDetails(legacySubjectId: String): AlcoholMonitoringOrderDetails {
    val orderDetails = orderDetailsRepository.findByLegacySubjectId(legacySubjectId)

    return AlcoholMonitoringOrderDetails(orderDetails)
  }

  fun getQueryExecutionId(criteria: OrderSearchCriteria, restricted: Boolean): String = orderDetailsRepository.searchOrders(criteria, restricted)

  fun getSearchResults(queryExecutionId: String, restricted: Boolean): List<AlcoholMonitoringOrderDetails> {
    val results = orderDetailsRepository.getSearchResults(queryExecutionId, restricted)
    return results.map { result -> AlcoholMonitoringOrderDetails(result) }
  }
}
