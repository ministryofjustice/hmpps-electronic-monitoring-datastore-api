package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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
  fun checkAvailability(restricted: Boolean = false): Boolean = searchRepository.listLegacyIds(restricted).count() > 0

  fun query(athenaQuery: AthenaStringQuery, restricted: Boolean): String {
    val result = searchRepository.runQuery(athenaQuery, restricted)

    return result
  }

  fun getQueryExecutionId(criteria: OrderSearchCriteria, restricted: Boolean): String = searchRepository.searchOrders(criteria, restricted)

  fun getSearchResults(queryExecutionId: String, restricted: Boolean): List<OrderSearchResult> {
    val results = searchRepository.getSearchResults(queryExecutionId, restricted)
    return results.map { result -> OrderSearchResult(result) }
  }

  fun getOrderInformation(legacySubjectId: String, restricted: Boolean): IntegrityOrderInformation {
    val keyOrderInformation = integrityOrderInformationRepository.getKeyOrderInformation(legacySubjectId, restricted)
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
