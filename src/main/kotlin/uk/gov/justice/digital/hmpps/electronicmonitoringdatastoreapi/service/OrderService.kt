package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQueryResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderRepository

@Service
class OrderService(
  @Autowired private val orderRepository: OrderRepository,
) {
  fun checkAvailability(role: AthenaRole): Boolean {
    val queryString: String = """
        SELECT 
          legacy_subject_id, 
          full_name, 
          primary_address_line_1, 
          primary_address_line_2, 
          primary_address_line_3, 
          primary_address_post_code, 
          order_start_date, 
          order_end_date 
        FROM 
          test_database.order_details
        WHERE 
    """.trimIndent()

    val athenaService = AthenaService()
    val resultSet = athenaService.getQueryResult(role, queryString)

    return resultSet.hasRows()
  }

  fun runAdhocQuery(query: String, role: AthenaRole): AthenaQueryResponse<String> {
    val result: String

    try {
      val athenaService = AthenaService()
      val resultSet: ResultSet = athenaService.getQueryResult(role, query)
      result = resultSet.toString()
    } catch (e: Exception) {
      return AthenaQueryResponse<String>(
        queryString = query,
        athenaRole = role.name,
        isErrored = true,
        errorMessage = e.message.toString(),
      )
    }

    return AthenaQueryResponse<String>(
      queryString = query,
      athenaRole = role.name,
      isErrored = false,
      queryResponse = result,
    )
  }

  fun search(criteria: OrderSearchCriteria, role: AthenaRole, fakeResponse: Boolean? = true): List<OrderSearchResult> {
    if (fakeResponse == true) {
      return OrderRepository.Companion.getFakeOrders()
    }

    val query = parseSearchCriteria(criteria)
    val orders = orderRepository.searchOrders(query, role)

    if (orders.queryResponse == null) {
      return emptyList()
    }

    return orders.queryResponse
  }

  companion object {
    fun parseSearchCriteria(criteria: OrderSearchCriteria): AthenaQuery {
      validateSearchCriteria(criteria)

      var existingCriteria = false

      val builder: StringBuilder = StringBuilder()
      builder.append(
        """
          SELECT 
            legacy_subject_id, 
            full_name, 
            primary_address_line_1, 
            primary_address_line_2, 
            primary_address_line_3, 
            primary_address_post_code, 
            order_start_date, 
            order_end_date 
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
  }
}
