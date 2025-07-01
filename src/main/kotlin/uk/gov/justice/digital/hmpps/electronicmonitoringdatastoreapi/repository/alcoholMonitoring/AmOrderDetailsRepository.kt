package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AmOrderDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmOrderDetailsDTO

@Service
class AmOrderDetailsRepository(
  val athenaClient: EmDatastoreClientInterface,
) {
  fun getOrderDetails(legacySubjectId: String): AthenaAmOrderDetailsDTO {
    val orderDetailsQuery = AmOrderDetailsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(orderDetailsQuery)

    val result = AthenaHelper.mapTo<AthenaAmOrderDetailsDTO>(athenaResponse)

    return result.first()
  }

  fun searchOrders(criteria: OrderSearchCriteria, restricted: Boolean): String {
    val orderSearchQuery = AmOrderDetailsQueryBuilder()
      .withLegacySubjectId(criteria.legacySubjectId)
      .withFirstName(criteria.firstName)
      .withLastName(criteria.lastName)
      .withAlias(criteria.alias)
      .withDob(criteria.dateOfBirth)

    return athenaClient.getQueryExecutionId(orderSearchQuery, restricted)
  }

  fun getSearchResults(queryExecutionId: String, restricted: Boolean): List<AthenaAmOrderDetailsDTO> {
    val resultSet = athenaClient.getQueryResult(queryExecutionId, restricted)
    val results = AthenaHelper.mapTo<AthenaAmOrderDetailsDTO>(resultSet)
    return results
  }
}
