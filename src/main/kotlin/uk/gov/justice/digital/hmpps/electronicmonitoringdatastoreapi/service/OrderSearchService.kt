package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.SearchRepository
import kotlin.String

@Service
class OrderSearchService(
  @field:Autowired val searchRepository: SearchRepository,
) {
  fun checkAvailability(restricted: Boolean = false): Boolean = searchRepository.listLegacyIds(restricted).count() > 0

  fun getQueryExecutionId(criteria: OrderSearchCriteria, restricted: Boolean): String = searchRepository.searchOrders(criteria, restricted)

  fun getSearchResults(queryExecutionId: String, restricted: Boolean): List<OrderSearchResult> {
    val results = searchRepository.getSearchResults(queryExecutionId, restricted)
    return results.map { result -> OrderSearchResult(result) }
  }
}
