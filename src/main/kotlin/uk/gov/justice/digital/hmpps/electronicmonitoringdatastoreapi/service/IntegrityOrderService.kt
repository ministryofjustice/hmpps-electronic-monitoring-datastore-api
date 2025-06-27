package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaStringQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityKeyOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegritySubjectHistoryReport
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.SearchRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityOrderDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityOrderInformationRepository
import kotlin.String

@Service
class IntegrityOrderService(
  @field:Autowired val searchRepository: SearchRepository,
  @field:Autowired val integrityOrderInformationRepository: IntegrityOrderInformationRepository,
  @field:Autowired val integrityOrderDetailsRepository: IntegrityOrderDetailsRepository,
) {
  fun checkAvailability(role: AthenaRole): Boolean {
    try {
      searchRepository.listLegacyIds(role)
    } catch (_: Exception) {
      return false
    }

    return true
  }

  fun query(athenaQuery: AthenaStringQuery, role: AthenaRole): String {
    val result = searchRepository.runQuery(athenaQuery, role)

    return result
  }

  fun getQueryExecutionId(criteria: OrderSearchCriteria, restricted: Boolean): String = searchRepository.searchOrders(criteria, restricted)

  fun getSearchResults(queryExecutionId: String, role: AthenaRole): List<OrderSearchResult> {
    val results = searchRepository.getSearchResults(queryExecutionId, role)
    return results.map { result -> OrderSearchResult(result) }
  }

  fun getOrderInformation(legacySubjectId: String, role: AthenaRole): IntegrityOrderInformation {
    val keyOrderInformation = integrityOrderInformationRepository.getKeyOrderInformation(legacySubjectId, role)
    val parsedIntegrityKeyOrderInformation = IntegrityKeyOrderInformation(keyOrderInformation)

    val emptyHistoryReport: IntegritySubjectHistoryReport = IntegritySubjectHistoryReport.createEmpty()
//    val subjectHistoryReport = orderInformationRepository.getSubjectHistoryReport(orderId, role)
//    val parsedSubjectHistoryReport = parseSubjectHistoryReport(subjectHistoryReport)

//    val documentList = orderInformationRepository.getDocumentList(orderId, role)
//    val parsedDocumentList = parseDocumentList(documentList)

    // Put it together
    return IntegrityOrderInformation(
      keyOrderInformation = parsedIntegrityKeyOrderInformation,
      subjectHistoryReport = emptyHistoryReport,
      documents = listOf(),
    )
  }

  fun getOrderDetails(legacySubjectId: String, restricted: Boolean): IntegrityOrderDetails {
    val orderDetailsDTO = integrityOrderDetailsRepository.getOrderDetails(legacySubjectId, restricted)
    return IntegrityOrderDetails(orderDetailsDTO)
  }
}
