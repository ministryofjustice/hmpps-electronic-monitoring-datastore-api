package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AmOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring.AmOrderDetailsRepository
import kotlin.String

@Service
class AmOrderDetailsService(
  val orderDetailsRepository: AmOrderDetailsRepository,
) {

  fun getOrderDetails(legacySubjectId: String): AmOrderDetails {
    val orderDetails = orderDetailsRepository.findByLegacySubjectId(legacySubjectId)

    return AmOrderDetails(orderDetails)
  }

  fun getQueryExecutionId(criteria: OrderSearchCriteria, restricted: Boolean): String = orderDetailsRepository.searchOrders(criteria, restricted)

  fun getSearchResults(queryExecutionId: String, restricted: Boolean): List<AmOrderDetails> {
    val results = orderDetailsRepository.getSearchResults(queryExecutionId, restricted)
    return results.map { result -> AmOrderDetails(result) }
  }
}
