package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AthenaOrderDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AthenaQueryResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Order
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.SearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaService

class OrderRepository {
  companion object {
    fun getFakeOrders(): List<Order> = listOf(
      Order(
        dataType = "am",
        legacySubjectId = 1000000,
        name = "Amy Smith",
        address = "First line of address",
        alias = null,
        dateOfBirth = "01-01-1970",
        orderStartDate = "08-02-2019",
        orderEndDate = "08-02-2020",
      ),
      Order(
        dataType = "am",
        legacySubjectId = 2000000,
        name = "Bill Smith",
        address = "First line of address",
        alias = "Plato",
        dateOfBirth = "01-02-1971",
        orderStartDate = "03-11-2020",
        orderEndDate = "03-11-2021",
      ),
      Order(
        dataType = "am",
        legacySubjectId = 3000000,
        name = "Claire Smith",
        address = "First line of address",
        alias = null,
        dateOfBirth = "09-04-1962",
        orderStartDate = "05-08-2001",
        orderEndDate = "05-08-2002",
      ),
      Order(
        dataType = "am",
        legacySubjectId = 8000000,
        name = "Daniel Smith",
        address = "First line of address",
        alias = "Aristotle",
        dateOfBirth = "12-11-1978",
        orderStartDate = "18-02-2012",
        orderEndDate = "18-02-2014",
      ),
      Order(
        dataType = "am",
        legacySubjectId = 30000,
        name = "Emma Smith",
        address = "First line of address",
        alias = "Socrates",
        dateOfBirth = "03-03-2001",
        orderStartDate = "24-01-2017",
        orderEndDate = "24-01-2020",
      ),
      Order(
        dataType = "am",
        legacySubjectId = 4000000,
        name = "Fred Smith",
        address = "First line of address",
        alias = null,
        dateOfBirth = "08-10-1980",
        orderStartDate = "01-05-2021",
        orderEndDate = "01-05-2022",
      ),
    )

    fun validateSearchCriteria(criteria: SearchCriteria): SearchCriteria {
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
          (criteria.legacySubjectId ?: "").toLong()
        } catch (e: Exception) {
          throw IllegalArgumentException("Legacy_subject_id must be convertable to type Long")
        }
      }

      // TODO: Handle Date of Birth, once it's in the data...

      return criteria
    }

    fun parseSearchCriteria(criteria: SearchCriteria): AthenaQuery {
      validateSearchCriteria(criteria)
      var existingCriteria: Boolean = false

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

    fun parseOrders(resultSet: ResultSet): List<Order> {
      var dtoOrders: List<AthenaOrderDTO> = AthenaHelper.mapTo<AthenaOrderDTO>(resultSet)

      var orders: List<Order> = dtoOrders.map { dto -> Order(dto) }
      return getFakeOrders() // TODO: Early return, testing/demo early.
      return orders

      // TODO: The field list being returned doesn't match 'order' object - this needs resolving asap!
      // Solution: use an OrderDTO object? I suspect this is the best approach.Or just map to an Order object that the UI needs
      // Probably: rename Order to OrderDTO and have an internal Order class that matches the SQL
    }
  }

  private val athenaService = AthenaService()

  fun getOrders(criteria: SearchCriteria): AthenaQueryResponse<List<Order>> {
    val athenaQuery: AthenaQuery = OrderRepository.parseSearchCriteria(criteria)
    val role = AthenaRole.DEV

    val athenaResponse: ResultSet = athenaService.getQueryResult(role, athenaQuery.queryString)

    val parsedOrders: List<Order> = parseOrders(athenaResponse)

    return AthenaQueryResponse<List<Order>>(
      queryString = athenaQuery.queryString,
      athenaRole = role.name,
      queryResponse = parsedOrders,
    )
  }
}
