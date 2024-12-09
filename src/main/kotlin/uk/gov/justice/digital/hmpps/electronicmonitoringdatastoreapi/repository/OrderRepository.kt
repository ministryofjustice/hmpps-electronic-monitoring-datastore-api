package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import software.amazon.awssdk.services.athena.model.ResultSet
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

    fun parseOrders(resultSet: ResultSet): List<Order> = getFakeOrders()

    fun parseSearchCriteria(criteria: SearchCriteria): AthenaQuery = AthenaQuery(
      queryString = """
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
            legacy_subject_id = ${criteria.legacySubjectId}
      """.trimIndent(),
    )
  }

  private val athenaService = AthenaService()

  fun getOrders(criteria: SearchCriteria): AthenaQueryResponse<List<Order>> {
    val athenaQuery: AthenaQuery = OrderRepository.parseSearchCriteria(criteria)
    val role = AthenaRole.DEV

    return try {
      val athenaResponse: ResultSet = athenaService.getQueryResult(role, athenaQuery.queryString)
      val parsedResult: List<Order> = parseOrders(athenaResponse)

      return AthenaQueryResponse<List<Order>>(
        queryString = athenaQuery.queryString,
        athenaRole = role.name,
        queryResponse = parsedResult,
      )
    } catch (e: Exception) {
      return AthenaQueryResponse(
        queryString = athenaQuery.queryString,
        athenaRole = role.name,
        isErrored = true,
        errorMessage = e.message.toString(),
      )
    }
  }
}
