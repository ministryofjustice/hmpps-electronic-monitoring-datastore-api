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

  fun query(athenaQuery: AthenaStringQuery, allowSpecials: Boolean = false): String {
    val result = searchRepository.runQuery(athenaQuery, allowSpecials)

    return result
  }

  fun getQueryExecutionId(criteria: OrderSearchCriteria, allowSpecials: Boolean = false): String = searchRepository.searchOrders(criteria, allowSpecials)

  fun getSearchResults(queryExecutionId: String, allowSpecials: Boolean = false): List<OrderSearchResult> {
    val results = searchRepository.getSearchResults(queryExecutionId, allowSpecials)
    return results.map { result -> OrderSearchResult(result) }
  }

  fun getOrderInformation(legacySubjectId: String, allowSpecials: Boolean = false): OrderInformation {
    val keyOrderInformation = orderInformationRepository.getKeyOrderInformation(legacySubjectId, allowSpecials)
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

  fun getOrderDetails(legacySubjectId: String, allowSpecials: Boolean = false): OrderDetails {
    val orderDetailsDTO: AthenaOrderDetailsDTO = orderDetailsRepository.getOrderDetails(legacySubjectId, allowSpecials)
    return OrderDetails(orderDetailsDTO)
  }
}
