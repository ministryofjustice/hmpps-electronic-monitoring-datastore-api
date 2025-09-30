package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityOrderDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityOrderDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria

@Service
class IntegrityOrderDetailsRepository(
  athenaClient: EmDatastoreClient,
) : AthenaRepository<AthenaIntegrityOrderDetailsDTO>(athenaClient) {
  override fun mapTo(results: ResultSet): List<AthenaIntegrityOrderDetailsDTO> = AthenaHelper.mapTo(results)

  fun getByLegacySubjectIdAndRestricted(legacySubjectId: String, restricted: Boolean): AthenaIntegrityOrderDetailsDTO = this.executeQuery(
    AthenaIntegrityOrderDetailsQueryBuilder().withLegacySubjectId(legacySubjectId),
    restricted,
  ).first()

  fun searchOrders(criteria: OrderSearchCriteria, restricted: Boolean): String {
    val orderSearchQuery = AthenaIntegrityOrderDetailsQueryBuilder()
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
