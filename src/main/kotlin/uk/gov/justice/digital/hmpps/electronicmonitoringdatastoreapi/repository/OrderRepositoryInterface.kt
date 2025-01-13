package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchResultDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRole

interface OrderRepositoryInterface {
  fun searchOrders(criteria: OrderSearchCriteria, role: AthenaRole): List<AthenaOrderSearchResultDTO>
  fun listLegacyIds(role: AthenaRole): List<String>
  fun runQuery(athenaQuery: AthenaQuery<*>, role: AthenaRole): String
}
