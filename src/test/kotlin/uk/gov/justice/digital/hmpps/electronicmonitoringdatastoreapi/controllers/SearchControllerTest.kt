package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AthenaQueryResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Order
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.SearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

class SearchControllerTest {
  private val auditService: AuditService = mock()

  private val sut: SearchController = SearchController(auditService)

//  @Nested
//  inner class GetSearchResult {
//
//    @Test
//    fun `Accepts caseId as parameter`() {
//      val caseId = "obviously-fake-data"
//      val expected = JSONObject(
//        mapOf("data" to "You have successfully queried case obviously-fake-data"),
//      )
//
//      val result: JSONObject = sut.getCases(caseId)
//      Assertions.assertThat(result.toString()).isEqualTo(expected.toString())
//    }
//  }

  @Nested
  inner class SearchOrdersFake {

    @Test
    fun `Returns a list of orders`() {
      // Arrange: Create search criteria
      val userToken = "fake-token"
      val searchCriteria = SearchCriteria(
        searchType = "name",
        legacySubjectId = "12345",
        firstName = "Amy",
        lastName = "Smith",
        alias = null,
        dobDay = "01",
        dobMonth = "01",
        dobYear = "1970",
      )

      val result: List<Order> = sut.searchOrdersFake(userToken, searchCriteria)

      // Assert: Verify the result is a list of Order objects
      assertThat(result).isNotEmpty // Ensure the result is not empty
      assertThat(result).allMatch { it is Order } // Ensure all elements are of type Order
    }
  }

  @Nested
  inner class QueryAthena {

    @Test
    fun `Returns response object of the correct type`() {
      val queryString = "fake query string"
      val queryRole = "fake-role"
      val queryObject = AthenaQuery(queryString = queryString)

      val expectedResult = AthenaQueryResponse<String>(
        queryString = queryString,
        isErrored = false,
        athenaRole = "fake",
      )

      val result: AthenaQueryResponse<String> = sut.queryAthena(queryRole, queryObject)

      assertThat(result).isInstanceOf(AthenaQueryResponse::class.java)
    }

    @Test
    fun `Returns error response`() {
      val queryString = "fake query string"
      val queryRole = "fake-role"
      val queryObject = AthenaQuery(queryString = queryString)

      val expectedResult = AthenaQueryResponse<String>(
        queryString = queryString,
        isErrored = true,
        athenaRole = "fake",
      )

      val result: AthenaQueryResponse<String> = sut.queryAthena(queryRole, queryObject)

      assertThat(result).isNotNull // Ensure the result is not empty
      assertThat(result.isErrored).isEqualTo(expectedResult.isErrored)
      assertThat(result.queryString).isEqualTo(expectedResult.queryString) // Ensure all elements are of type Order
    }
  }
}
