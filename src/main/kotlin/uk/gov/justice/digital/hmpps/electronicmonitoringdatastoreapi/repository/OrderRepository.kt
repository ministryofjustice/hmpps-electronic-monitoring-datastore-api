package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.ListKeyOrderInformationQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SearchKeyOrderInformationQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchResultDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaService

@Service
@Profile("!test")
class OrderRepository(
  @Autowired val athenaClient: AthenaService,
) : OrderRepositoryInterface {
  override fun searchOrders(criteria: OrderSearchCriteria, role: AthenaRole): List<AthenaOrderSearchResultDTO> {
    val searchKeyOrderInformationQuery = SearchKeyOrderInformationQueryBuilder()
      .withLegacySubjectId(criteria.legacySubjectId)
      .withFirstName(criteria.firstName)
      .withLastName(criteria.lastName)
      .withAlias(criteria.alias)
      .build()

    val athenaResponse = athenaClient.getQueryResult(role, searchKeyOrderInformationQuery.queryString)

    val result = AthenaHelper.mapTo<AthenaOrderSearchResultDTO>(athenaResponse)

    return result
  }

  override fun listLegacyIds(role: AthenaRole): List<String> {
    val athenaQuery = ListKeyOrderInformationQueryBuilder().build()

    val athenaResponse = athenaClient.getQueryResult(role, athenaQuery.queryString)

    val result = AthenaHelper.mapTo<String>(athenaResponse)

    return result
  }

  override fun runQuery(athenaQuery: AthenaQuery, role: AthenaRole): String {
    val athenaResponse = athenaClient.getQueryResult(role, athenaQuery.queryString)

    val result = athenaResponse.toString()

    return result
  }
}
