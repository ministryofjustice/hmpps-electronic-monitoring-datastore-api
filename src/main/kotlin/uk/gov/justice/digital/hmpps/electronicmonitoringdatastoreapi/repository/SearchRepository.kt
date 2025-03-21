package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.ListKeyOrderInformationQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.OrderSearchQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchResultDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaStringQuery

@Service
class SearchRepository(
  @Autowired val athenaClient: EmDatastoreClientInterface,
  @Value("\${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {
  fun searchOrders(criteria: OrderSearchCriteria, allowSpecials: Boolean): String {
    val tableName = AthenaHelper.tableNameFromSearchType(criteria.searchType)
    val orderSearchQuery = OrderSearchQueryBuilder(athenaDatabase, tableName)
      .withLegacySubjectId(criteria.legacySubjectId)
      .withFirstName(criteria.firstName)
      .withLastName(criteria.lastName)
      .withAlias(criteria.alias)
      .withDob(criteria.dobDay, criteria.dobMonth, criteria.dobYear)
      .build()

    return athenaClient.getQueryExecutionId(orderSearchQuery, allowSpecials)
  }

  fun getSearchResults(queryExecutionId: String, allowSpecials: Boolean): List<AthenaOrderSearchResultDTO> {
    val resultSet = athenaClient.getQueryResult(queryExecutionId, allowSpecials)
    val results = AthenaHelper.mapTo<AthenaOrderSearchResultDTO>(resultSet)
    return results
  }

  fun listLegacyIds(allowSpecials: Boolean): List<String> {
    val athenaQuery = ListKeyOrderInformationQueryBuilder(athenaDatabase).build()

    val athenaResponse: ResultSet = athenaClient.getQueryResult(athenaQuery, allowSpecials)

    data class SubjectId(
      val legacySubjectId: String,
    )

    val result = AthenaHelper.mapTo<SubjectId>(athenaResponse)

    return result.map { it.legacySubjectId }
  }

  fun runQuery(athenaQuery: AthenaStringQuery, allowSpecials: Boolean): String {
    val athenaResponse = athenaClient.getQueryResult(athenaQuery, allowSpecials)

    val result = athenaResponse.toString()

    return result
  }
}
