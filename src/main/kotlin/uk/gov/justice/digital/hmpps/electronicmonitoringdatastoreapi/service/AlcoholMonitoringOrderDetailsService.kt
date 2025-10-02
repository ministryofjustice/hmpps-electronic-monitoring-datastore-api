package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.AlcoholMonitoringOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.AlcoholMonitoringOrderDetailsRepository
import kotlin.String

@Service
@Validated
class AlcoholMonitoringOrderDetailsService(
  val orderDetailsRepository: AlcoholMonitoringOrderDetailsRepository,
) {

  fun getOrderDetails(legacySubjectId: String): AlcoholMonitoringOrderDetails {
    val orderDetails = orderDetailsRepository.getByLegacySubjectId(legacySubjectId)

    return AlcoholMonitoringOrderDetails(orderDetails)
  }

  fun getQueryExecutionId(@Valid criteria: OrderSearchCriteria): String = orderDetailsRepository.searchOrders(criteria)

  fun getSearchResults(queryExecutionId: String): List<AlcoholMonitoringOrderDetails> {
    val results = orderDetailsRepository.getSearchResults(queryExecutionId)
    return results.map { result -> AlcoholMonitoringOrderDetails(result) }
  }
}
