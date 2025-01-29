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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderRepository
import kotlin.String

@Service
class OrderService(
  @Autowired val orderRepository: OrderRepository,
  @Autowired val orderInformationRepository: OrderInformationRepository,
  @Autowired val orderDetailsRepository: OrderDetailsRepository,
) {
  fun checkAvailability(role: AthenaRole): Boolean {
    try {
      orderRepository.listLegacyIds(role)
    } catch (_: Exception) {
      return false
    }

    return true
  }

  fun query(athenaQuery: AthenaStringQuery, role: AthenaRole): String {
    val result = orderRepository.runQuery(athenaQuery, role)

    return result
  }

  fun search(criteria: OrderSearchCriteria, role: AthenaRole): List<OrderSearchResult> {
    val orders = orderRepository.searchOrders(criteria, role)

    val parsedOrderSearchResults = orders.map { athenaOrderSearchResult -> OrderSearchResult(athenaOrderSearchResult) }

    return parsedOrderSearchResults
  }

  fun getOrderInformation(orderId: String, role: AthenaRole): OrderInformation {
    val keyOrderInformation = orderInformationRepository.getKeyOrderInformation(orderId, role)
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

  fun getOrderDetails(orderId: String, role: AthenaRole): OrderDetails {
    val orderDetailsDTO: AthenaOrderDetailsDTO = orderDetailsRepository.getOrderDetails(orderId, role)
    return OrderDetails(orderDetailsDTO)
  }
}
