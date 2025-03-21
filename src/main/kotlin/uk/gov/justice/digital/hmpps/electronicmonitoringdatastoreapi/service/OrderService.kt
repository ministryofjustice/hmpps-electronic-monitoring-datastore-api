package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Document
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.KeyOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.SubjectHistoryReport
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaStringQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderInformationRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.SearchRepository
import kotlin.String

@Service
class OrderService(
  @Autowired val searchRepository: SearchRepository,
  @Autowired val orderInformationRepository: OrderInformationRepository,
  @Autowired val orderDetailsRepository: OrderDetailsRepository,
) {
  fun checkAvailability(): Boolean {
    try {
      searchRepository.listLegacyIds(false)
    } catch (_: Exception) {
      return false
    }

    return true
  }

  fun query(athenaQuery: AthenaStringQuery, allowSpecials: Boolean): String {
    val result = searchRepository.runQuery(athenaQuery, allowSpecials)

    return result
  }

  fun getQueryExecutionId(criteria: OrderSearchCriteria, allowSpecials: Boolean): String = searchRepository.searchOrders(criteria, allowSpecials)

  fun getSearchResults(executionId: String, allowSpecials: Boolean): List<OrderSearchResult> {
    val results = searchRepository.getSearchResults(executionId, allowSpecials)
    return results.map { result -> OrderSearchResult(result) }
  }

  fun getOrderInformation(orderId: String, allowSpecials: Boolean): OrderInformation {
    val keyOrderInformation = orderInformationRepository.getKeyOrderInformation(orderId, allowSpecials)
    val parsedKeyOrderInformation = KeyOrderInformation(keyOrderInformation)

    val emptyHistoryReport: SubjectHistoryReport = SubjectHistoryReport.createEmpty()
//    val subjectHistoryReport = orderInformationRepository.getSubjectHistoryReport(orderId, role)
//    val parsedSubjectHistoryReport = parseSubjectHistoryReport(subjectHistoryReport)

//    val documentList = orderInformationRepository.getDocumentList(orderId, role)
//    val parsedDocumentList = parseDocumentList(documentList)

    // Put it together
    return OrderInformation(
      keyOrderInformation = parsedKeyOrderInformation,
      subjectHistoryReport = emptyHistoryReport,
      documents = listOf<Document>(),
    )
  }

  fun getOrderDetails(orderId: String, allowSpecials: Boolean): OrderDetails {
    val orderDetailsDTO: AthenaOrderDetailsDTO = orderDetailsRepository.getOrderDetails(orderId, allowSpecials)
    return OrderDetails(orderDetailsDTO)
  }
}
