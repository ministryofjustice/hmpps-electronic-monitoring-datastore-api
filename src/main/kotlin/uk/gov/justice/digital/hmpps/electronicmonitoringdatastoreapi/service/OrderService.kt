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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchResultDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaStringQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSubjectHistoryReportDTO
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

  private fun parseOrderSearchResults(athenaOrderSearchResultList: List<AthenaOrderSearchResultDTO>): List<OrderSearchResult> {
    var orderSearchResults = athenaOrderSearchResultList.map { athenaOrderSearchResult -> OrderSearchResult(athenaOrderSearchResult) }

    // TODO: The field list being returned doesn't match 'order' object - this needs resolving asap!
    // Solution: use an OrderDTO object? I suspect this is the best approach.Or just map to an Order object that the UI needs
    // Probably: rename Order to OrderDTO and have an internal Order class that matches the SQL
    return orderSearchResults
  }

  private fun parseKeyOrderInformation(athenaKeyOrderInformation: AthenaKeyOrderInformationDTO): KeyOrderInformation {
    var keyOrderInformation = KeyOrderInformation(athenaKeyOrderInformation)

    return keyOrderInformation
  }

  private fun parseSubjectHistoryReport(athenaSubjectHistoryReport: AthenaSubjectHistoryReportDTO): SubjectHistoryReport {
    var subjectHistoryReport = SubjectHistoryReport(
      name = athenaSubjectHistoryReport.name,
      reportUrl = athenaSubjectHistoryReport.reportUrl,
      createdOn = athenaSubjectHistoryReport.createdOn,
      time = athenaSubjectHistoryReport.time,
    )

    return subjectHistoryReport
  }

  private fun parseDocumentList(athenaDocumentList: AthenaDocumentListDTO): DocumentList {
    var documentList = DocumentList(
      pageSize = athenaDocumentList.pageSize,
      orderDocuments = athenaDocumentList.orderDocuments.map { document ->
        Document(
          name = document.name,
          url = document.url,
          notes = document.notes,
          createdOn = document.createdOn,
          time = document.time,
        )
      },
    )

    return documentList
  }
}
