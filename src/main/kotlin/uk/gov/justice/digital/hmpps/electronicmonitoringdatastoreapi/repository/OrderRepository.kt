package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchResultDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQueryResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaService

@Service
class OrderRepository {
  companion object {
    fun getFakeOrders(): List<OrderSearchResult> = listOf(
      OrderSearchResult(
        dataType = "am",
        legacySubjectId = 1000000,
        name = "Amy Smith",
        address = "First line of address",
        alias = null,
        dateOfBirth = "01-01-1970",
        orderStartDate = "08-02-2019",
        orderEndDate = "08-02-2020",
      ),
      OrderSearchResult(
        dataType = "am",
        legacySubjectId = 2000000,
        name = "Bill Smith",
        address = "First line of address",
        alias = "Plato",
        dateOfBirth = "01-02-1971",
        orderStartDate = "03-11-2020",
        orderEndDate = "03-11-2021",
      ),
      OrderSearchResult(
        dataType = "am",
        legacySubjectId = 3000000,
        name = "Claire Smith",
        address = "First line of address",
        alias = null,
        dateOfBirth = "09-04-1962",
        orderStartDate = "05-08-2001",
        orderEndDate = "05-08-2002",
      ),
      OrderSearchResult(
        dataType = "am",
        legacySubjectId = 8000000,
        name = "Daniel Smith",
        address = "First line of address",
        alias = "Aristotle",
        dateOfBirth = "12-11-1978",
        orderStartDate = "18-02-2012",
        orderEndDate = "18-02-2014",
      ),
      OrderSearchResult(
        dataType = "am",
        legacySubjectId = 30000,
        name = "Emma Smith",
        address = "First line of address",
        alias = "Socrates",
        dateOfBirth = "03-03-2001",
        orderStartDate = "24-01-2017",
        orderEndDate = "24-01-2020",
      ),
      OrderSearchResult(
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

    fun parseOrders(resultSet: ResultSet): List<OrderSearchResult> {
      var dtoOrders: List<AthenaOrderSearchResultDTO> = AthenaHelper.mapTo<AthenaOrderSearchResultDTO>(resultSet)

      var orderSearchResults: List<OrderSearchResult> = dtoOrders.map { dto -> OrderSearchResult(dto) }
//      return getFakeOrders() // TODO: Early return, testing/demo early.
      return orderSearchResults

      // TODO: The field list being returned doesn't match 'order' object - this needs resolving asap!
      // Solution: use an OrderDTO object? I suspect this is the best approach.Or just map to an Order object that the UI needs
      // Probably: rename Order to OrderDTO and have an internal Order class that matches the SQL
    }
  }

  private val athenaService = AthenaService()

  fun searchOrders(athenaQuery: AthenaQuery, role: AthenaRole): AthenaQueryResponse<List<OrderSearchResult>> {
    val queryString = athenaQuery.queryString
    val athenaResponse: ResultSet = athenaService.getQueryResult(role, queryString)

    val parsedOrderSearchResults: List<OrderSearchResult> = parseOrders(athenaResponse)

    return AthenaQueryResponse<List<OrderSearchResult>>(
      queryString = queryString,
      athenaRole = role.name,
      queryResponse = parsedOrderSearchResults,
    )
  }
}
