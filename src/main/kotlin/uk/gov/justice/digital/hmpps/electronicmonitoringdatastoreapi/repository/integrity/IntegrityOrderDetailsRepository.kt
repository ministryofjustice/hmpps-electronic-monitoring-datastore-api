package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityOrderDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityOrderDetailsDTO

@Service
class IntegrityOrderDetailsRepository(
  val athenaClient: EmDatastoreClientInterface,
) {
  fun getOrderDetails(legacySubjectId: String, restricted: Boolean): AthenaIntegrityOrderDetailsDTO {
    val orderDetailsQuery = IntegrityOrderDetailsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(orderDetailsQuery, restricted)

    val result = AthenaHelper.mapTo<AthenaIntegrityOrderDetailsDTO>(athenaResponse)

    return result.first()
  }

  fun searchOrders(criteria: OrderSearchCriteria, restricted: Boolean): String {
    val orderSearchQuery = IntegrityOrderDetailsQueryBuilder()
      .withLegacySubjectId(criteria.legacySubjectId)
      .withFirstName(criteria.firstName)
      .withLastName(criteria.lastName)
      .withAlias(criteria.alias)
      .withDob(criteria.dateOfBirth)

    return athenaClient.getQueryExecutionId(orderSearchQuery, restricted)
  }

  fun getSearchResults(queryExecutionId: String, restricted: Boolean): List<AthenaIntegrityOrderDetailsDTO> {
    val resultSet = athenaClient.getQueryResult(queryExecutionId, restricted)
    val results = AthenaHelper.mapTo<AthenaIntegrityOrderDetailsDTO>(resultSet)
    return results
  }
}
