package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AlcoholMonitoringOrderDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.alcoholMonitoring.AthenaAlcoholMonitoringOrderDetailsDTO

@Service
class AlcoholMonitoringOrderDetailsRepository(
  val athenaClient: EmDatastoreClientInterface,
) {
  fun findByLegacySubjectId(legacySubjectId: String): AthenaAlcoholMonitoringOrderDetailsDTO {
    val orderDetailsQuery = AlcoholMonitoringOrderDetailsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(orderDetailsQuery)

    val result = AthenaHelper.mapTo<AthenaAlcoholMonitoringOrderDetailsDTO>(athenaResponse)

    return result.first()
  }

  fun searchOrders(criteria: OrderSearchCriteria, restricted: Boolean): String {
    val orderSearchQuery = AlcoholMonitoringOrderDetailsQueryBuilder()
      .withLegacySubjectId(criteria.legacySubjectId)
      .withFirstName(criteria.firstName)
      .withLastName(criteria.lastName)
      .withAlias(criteria.alias)
      .withDob(criteria.dateOfBirth)

    return athenaClient.getQueryExecutionId(orderSearchQuery, restricted)
  }

  fun getSearchResults(queryExecutionId: String, restricted: Boolean): List<AthenaAlcoholMonitoringOrderDetailsDTO> {
    val resultSet = athenaClient.getQueryResult(queryExecutionId, restricted)
    val results = AthenaHelper.mapTo<AthenaAlcoholMonitoringOrderDetailsDTO>(resultSet)
    return results
  }
}
