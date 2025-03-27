package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
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

  fun getQueryExecutionId(criteria: OrderSearchCriteria, role: AthenaRole): String = searchRepository.searchOrders(criteria, role)

  fun getSearchResults(executionId: String, role: AthenaRole): List<OrderSearchResult> {
    val results = searchRepository.getSearchResults(executionId, role)
    return results.map { result -> OrderSearchResult(result) }
  }

  fun getOrderInformation(legacySubjectId: String, role: AthenaRole): OrderInformation {
    val keyOrderInformation = orderInformationRepository.getKeyOrderInformation(legacySubjectId, role)
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

  fun getOrderDetails(legacySubjectId: String, role: AthenaRole): OrderDetails {
    val orderDetailsDTO: AthenaOrderDetailsDTO = orderDetailsRepository.getOrderDetails(legacySubjectId, role)
    return OrderDetails(orderDetailsDTO)
  }
}
