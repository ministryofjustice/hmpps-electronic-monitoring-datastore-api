package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaAlcoholMonitoringOrderDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaAlcoholMonitoringOrderDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria

@Service
class AlcoholMonitoringOrderDetailsRepository(
  athenaClient: EmDatastoreClient,
) : AthenaRepository<AthenaAlcoholMonitoringOrderDetailsDTO>(athenaClient) {
  override fun mapTo(results: ResultSet): List<AthenaAlcoholMonitoringOrderDetailsDTO> = AthenaHelper.mapTo(results)

  fun getByLegacySubjectId(legacySubjectId: String): AthenaAlcoholMonitoringOrderDetailsDTO = this.executeQuery(
    AthenaAlcoholMonitoringOrderDetailsQueryBuilder().withLegacySubjectId(legacySubjectId),
  ).first()

  fun searchOrders(criteria: OrderSearchCriteria): String {
    val orderSearchQuery = AthenaAlcoholMonitoringOrderDetailsQueryBuilder()
      .withLegacySubjectId(criteria.legacySubjectId)
      .withFirstName(criteria.firstName)
      .withLastName(criteria.lastName)
      .withAlias(criteria.alias)
      .withDob(criteria.dateOfBirth)

    return athenaClient.getQueryExecutionId(orderSearchQuery)
  }

  fun getSearchResults(queryExecutionId: String): List<AthenaAlcoholMonitoringOrderDetailsDTO> {
    val resultSet = athenaClient.getQueryResult(queryExecutionId)
    return mapTo(resultSet)
  }
}
