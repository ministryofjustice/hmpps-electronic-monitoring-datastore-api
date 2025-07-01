package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.ListKeyOrderInformationQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.OrderSearchQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchResultDTO

@Service
class SearchRepository(
  @field:Autowired val athenaClient: EmDatastoreClientInterface,
) {
  fun searchOrders(criteria: OrderSearchCriteria, restricted: Boolean): String {
    val tableName = AthenaHelper.tableNameFromSearchType(criteria.searchType)
    val orderSearchQuery = OrderSearchQueryBuilder(tableName)
      .withLegacySubjectId(criteria.legacySubjectId)
      .withFirstName(criteria.firstName)
      .withLastName(criteria.lastName)
      .withAlias(criteria.alias)
      .withDob(criteria.dateOfBirth)

    return athenaClient.getQueryExecutionId(orderSearchQuery, restricted)
  }

  fun getSearchResults(queryExecutionId: String, restricted: Boolean): List<AthenaOrderSearchResultDTO> {
    val resultSet = athenaClient.getQueryResult(queryExecutionId, restricted)
    val results = AthenaHelper.mapTo<AthenaOrderSearchResultDTO>(resultSet)
    return results
  }

  fun listLegacyIds(restricted: Boolean): List<String> {
    val athenaQuery = ListKeyOrderInformationQueryBuilder()

    val athenaResponse: ResultSet = athenaClient.getQueryResult(athenaQuery, restricted)

    data class SubjectId(
      val legacySubjectId: String,
    )

    val result = AthenaHelper.mapTo<SubjectId>(athenaResponse)

    return result.map { it.legacySubjectId }
  }
}
