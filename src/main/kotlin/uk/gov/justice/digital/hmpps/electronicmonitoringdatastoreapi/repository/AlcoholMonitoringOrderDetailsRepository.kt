package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.athena.AthenaRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.AmOrderDetails

@Service
class AlcoholMonitoringOrderDetailsRepository(
  athenaClient: EmDatastoreClient,
) : AthenaRepository<AmOrderDetails>(athenaClient, AmOrderDetails::class) {
  fun searchOrders(criteria: OrderSearchCriteria): String {
    @Suppress("UNCHECKED_CAST")
    val orderSearchQuery = queryBuilder().findBy(criteria)
    return athenaClient.getQueryExecutionId(orderSearchQuery)
  }

  fun getSearchResults(queryExecutionId: String): List<AmOrderDetails> {
    val resultSet = athenaClient.getQueryResult(queryExecutionId)
    return mapTo(resultSet)
  }
}
