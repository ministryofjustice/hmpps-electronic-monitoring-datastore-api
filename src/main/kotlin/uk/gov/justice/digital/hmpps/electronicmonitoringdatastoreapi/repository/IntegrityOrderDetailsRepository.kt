package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.OrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.athena.AthenaRepository

@Service
class IntegrityOrderDetailsRepository(
  athenaClient: EmDatastoreClient,
) : AthenaRepository<OrderDetails>(athenaClient, OrderDetails::class) {
  fun searchOrders(criteria: OrderSearchCriteria, restricted: Boolean): String {
    @Suppress("UNCHECKED_CAST")
    val orderSearchQuery = queryBuilder().findBy(criteria)
    return athenaClient.getQueryExecutionId(orderSearchQuery, restricted)
  }

  fun getSearchResults(queryExecutionId: String, restricted: Boolean): List<OrderDetails> {
    val resultSet = athenaClient.getQueryResult(queryExecutionId, restricted)
    return mapTo(resultSet)
  }
}
