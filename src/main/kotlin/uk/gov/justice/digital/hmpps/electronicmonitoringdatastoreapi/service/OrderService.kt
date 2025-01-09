package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Document
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.DocumentList
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.KeyOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.SubjectHistoryReport
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchResultDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSubjectHistoryReportDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderInformationRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderInformationRepositoryInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderRepository
import kotlin.String

@Service
class OrderService(
  @Autowired val orderRepository: OrderRepository,
  @Autowired val orderInformationRepository: OrderInformationRepositoryInterface,
) {
  fun checkAvailability(role: AthenaRole): Boolean {
    val athenaQuery = listKeyOrderInformationQuery()

    val results = orderRepository.runQuery(athenaQuery, role)
    val result = results.isNotEmpty()

    return result
  }

  fun query(athenaQuery: AthenaQuery, role: AthenaRole): String {
    val result = orderRepository.runQuery(athenaQuery, role)

    return result
  }

  fun search(criteria: OrderSearchCriteria, role: AthenaRole, fakeResponse: Boolean? = true): List<OrderSearchResult> {
    if (fakeResponse == true) {
      return OrderRepository.getFakeOrders()
    }

    val query = searchKeyOrderInformationQuery(criteria)
    val orders = orderRepository.searchOrders(query, role)

    val parsedOrderSearchResults = parseOrderSearchResults(orders)

    return parsedOrderSearchResults
  }

  fun getOrderInformation(orderId: String, role: AthenaRole): OrderInformation {
    val keyOrderInformationQuery = getKeyOrderInformationQuery(orderId)
    val keyOrderInformation = orderInformationRepository.getKeyOrderInformation(keyOrderInformationQuery, role)
    val parsedKeyOrderInformation = parseKeyOrderInformation(keyOrderInformation)

    val subjectHistoryReportQuery = getSubjectHistoryReportQuery(orderId)
    val subjectHistoryReport = orderInformationRepository.getSubjectHistoryReport(subjectHistoryReportQuery, role)
    val parsedSubjectHistoryReport = parseSubjectHistoryReport(subjectHistoryReport)

    val documentListQuery = getDocumentListQuery(orderId)
    val documentList = orderInformationRepository.getDocumentList(documentListQuery, role)
    val parsedDocumentList = parseDocumentList(documentList)

    // Put it together
    return OrderInformation(
      keyOrderInformation = parsedKeyOrderInformation,
      subjectHistoryReport = parsedSubjectHistoryReport,
      documents = parsedDocumentList,
    )
  }

  companion object {
    fun searchKeyOrderInformationQuery(criteria: OrderSearchCriteria): AthenaQuery {
      validateSearchCriteria(criteria)

      var existingCriteria = false

      val builder: StringBuilder = StringBuilder()
      builder.append(
        """
          SELECT
            legacy_subject_id
            , full_name
            , primary_address_line_1
            , primary_address_line_2
            , primary_address_line_3
            , primary_address_post_code
            , order_start_date
            , order_end_date
          FROM
            test_database.order_details
          WHERE 
        """.trimIndent(),
      )

      if (criteria.legacySubjectId != null) {
        builder.append("legacy_subject_id = ${criteria.legacySubjectId}")
        existingCriteria = true
      }

      if (!criteria.firstName.isNullOrBlank()) {
        builder.append("${if (existingCriteria) " OR" else ""} upper(first_name) = upper('${criteria.firstName}')")
        existingCriteria = true
      }

      if (!criteria.lastName.isNullOrBlank()) {
        builder.append("${if (existingCriteria) " OR" else ""} upper(last_name) = upper('${criteria.lastName}')")
        existingCriteria = true
      }

      if (!criteria.alias.isNullOrBlank()) {
        builder.append("${if (existingCriteria) " OR" else ""} upper(alias) = upper('${criteria.alias}')")
        existingCriteria = true
      }

      builder.toString()

      return AthenaQuery(
        queryString = builder.toString(),
      )
    }

    private fun validateSearchCriteria(criteria: OrderSearchCriteria): OrderSearchCriteria {
      if (criteria.legacySubjectId == null &&
        criteria.firstName == null &&
        criteria.lastName == null &&
        criteria.alias == null &&
        criteria.dobDay == null &&
        criteria.dobMonth == null &&
        criteria.dobYear == null
      ) {
        throw IllegalArgumentException("At least one search criteria must be populated")
      }

      if (criteria.legacySubjectId != null) {
        try {
          criteria.legacySubjectId.toLong()
        } catch (_: Exception) {
          throw IllegalArgumentException("Legacy_subject_id must be convertable to type Long")
        }
      }

      // TODO: Handle Date of Birth, once it's in the data...

      return criteria
    }

    fun getKeyOrderInformationQuery(orderId: String): AthenaQuery = AthenaQuery(
      queryString = """
        SELECT
              legacy_subject_id
            , legacy_order_id
            , full_name
            , alias
            , date_of_birth
            , primary_address_line_1
            , primary_address_line_2
            , primary_address_line_3
            , primary_address_post_code
            , order_start_date
            , order_end_date
        FROM 
          test_database.order_details
        WHERE
          legacy_subject_id = $orderId
      """.trimIndent(),
    )

    fun getSubjectHistoryReportQuery(orderId: String): AthenaQuery = AthenaQuery(
      queryString = """
        SELECT
           $orderId as legacy_order_id
      """.trimIndent(),
    )

    fun getDocumentListQuery(orderId: String): AthenaQuery = AthenaQuery(
      queryString = """
        SELECT
           $orderId as legacy_order_id
      """.trimIndent(),
    )

    fun listKeyOrderInformationQuery(): AthenaQuery = AthenaQuery(
      queryString = """
        SELECT 
          legacy_subject_id
        FROM 
          test_database.order_details
      """.trimIndent(),
    )
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
      orderDocuments = athenaDocumentList.orderDocuments.map {
        document -> Document(
          name = document.name,
          url = document.url,
          notes = document.notes,
          createdOn = document.createdOn,
          time = document.time,
        )
      }
    )

    return documentList
  }
}
