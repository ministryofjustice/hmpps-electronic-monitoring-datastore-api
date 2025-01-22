package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.ListKeyOrderInformationQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SearchKeyOrderInformationQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchResultDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaStringQuery

@Service
class OrderRepository(
  @Autowired val athenaClient: EmDatastoreClientInterface,
) {
  fun searchOrders(criteria: OrderSearchCriteria, role: AthenaRole): List<AthenaOrderSearchResultDTO> {
    val searchKeyOrderInformationQuery = SearchKeyOrderInformationQueryBuilder()
      .withLegacySubjectId(criteria.legacySubjectId)
      .withFirstName(criteria.firstName)
      .withLastName(criteria.lastName)
      .withAlias(criteria.alias)
      .build()

    val athenaResponse = athenaClient.getQueryResult(searchKeyOrderInformationQuery, role)

    val result = AthenaHelper.mapTo<AthenaOrderSearchResultDTO>(athenaResponse)

    return result
  }

  fun listLegacyIds(role: AthenaRole): List<String> {
    val athenaQuery = ListKeyOrderInformationQueryBuilder().build()

    val athenaResponse = athenaClient.getQueryResult(athenaQuery, role)

    val result = AthenaHelper.mapTo<String>(athenaResponse)

    return result
  }

  fun runQuery(athenaQuery: AthenaStringQuery, role: AthenaRole): String {
    val athenaResponse = athenaClient.getQueryResult(athenaQuery, role)

    val result = athenaResponse.toString()

    return result
  }
}
