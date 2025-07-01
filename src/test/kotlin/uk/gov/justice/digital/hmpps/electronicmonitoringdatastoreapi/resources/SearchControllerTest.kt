package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.security.core.Authentication
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.QueryExecutionResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.SearchController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderSearchService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDate
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class SearchControllerTest {
  private lateinit var orderSearchService: OrderSearchService
  private lateinit var auditService: AuditService
  private lateinit var controller: SearchController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    orderSearchService = Mockito.mock(OrderSearchService::class.java)
    auditService = Mockito.mock(AuditService::class.java)
    controller = SearchController(orderSearchService, auditService)
  }

  @Nested
  inner class SearchOrders {
    @Test
    fun `get a query execution ID from order service`() {
      val orderSearchCriteria = OrderSearchCriteria(
        searchType = "name",
        legacySubjectId = "12345",
        firstName = "Amy",
        lastName = "Smith",
        alias = null,
        dateOfBirth = LocalDate.parse("1970-01-01"),
      )

      val queryExecutionId = "query-execution-id"

      val expectedResult = QueryExecutionResponse(
        queryExecutionId = queryExecutionId,
      )

      Mockito.`when`(orderSearchService.getQueryExecutionId(orderSearchCriteria, false)).thenReturn(queryExecutionId)

      val result = controller.executeSearch(authentication, orderSearchCriteria)

      assertThat(result.body).isEqualTo(expectedResult)
    }
  }

  @Nested
  inner class GetSearchResult {
    @Test
    fun `find list of orders from order service`() {
      val queryExecutionId = "query-execution-id"

      val expectedResult = listOf(
        OrderSearchResult(
          dataType = "am",
          legacySubjectId = "12345",
          firstName = "Amy",
          lastName = "Smith",
          addressLine1 = "First line of address",
          addressLine2 = "Second line of address",
          addressLine3 = "Third line of address",
          addressPostcode = "A0000A",
          alias = null,
          dateOfBirth = LocalDateTime.parse("1970-01-01T00:00:00"),
          orderStartDate = LocalDateTime.parse("2019-02-08T00:00:00"),
          orderEndDate = LocalDateTime.parse("2020-02-08T00:00:00"),
        ),
      )

      Mockito.`when`(orderSearchService.getSearchResults(queryExecutionId, false)).thenReturn(expectedResult)

      val result = controller.getSearchResults(authentication, queryExecutionId)

      assertThat(result.body).isNotEmpty
      assertThat(result.body).isEqualTo(expectedResult)
    }
  }
}
