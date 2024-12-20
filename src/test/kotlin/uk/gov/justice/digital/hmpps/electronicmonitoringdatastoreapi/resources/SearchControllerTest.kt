package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.security.core.Authentication
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQueryResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.SearchController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@ActiveProfiles("test")
@JsonTest
class SearchControllerTest {
  private lateinit var auditService: AuditService
  private lateinit var controller: SearchController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    auditService = mock()
    controller = SearchController(auditService)
    authentication = mock(Authentication::class.java)
    `when`(authentication.principal).thenReturn("MOCK_AUTH_USER")
  }

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
      val orderSearchCriteria = OrderSearchCriteria(
        searchType = "name",
        legacySubjectId = "12345",
        firstName = "Amy",
        lastName = "Smith",
        alias = null,
        dobDay = "01",
        dobMonth = "01",
        dobYear = "1970",
      )

      val result: List<OrderSearchResult> = controller.searchOrdersFake(authentication, orderSearchCriteria)

      // Assert: Verify the result is a list of Order objects
      assertThat(result).isNotEmpty // Ensure the result is not empty
      assertThat(result).allMatch { it is OrderSearchResult } // Ensure all elements are of type Order
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

      val result: AthenaQueryResponse<String> = controller.queryAthena(authentication, queryObject, queryRole)

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

      val result: AthenaQueryResponse<String> = controller.queryAthena(authentication, queryObject, queryRole)

      assertThat(result).isNotNull // Ensure the result is not empty
      assertThat(result.isErrored).isEqualTo(expectedResult.isErrored)
      assertThat(result.queryString).isEqualTo(expectedResult.queryString) // Ensure all elements are of type Order
    }
  }
}
