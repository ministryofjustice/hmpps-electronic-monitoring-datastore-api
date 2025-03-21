package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources.integrity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.security.core.Authentication
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.QueryExecutionResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.integrity.SearchController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class SearchControllerTest {
  private lateinit var orderService: OrderService
  private lateinit var auditService: AuditService
  private lateinit var controller: SearchController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    orderService = Mockito.mock(OrderService::class.java)
    auditService = Mockito.mock(AuditService::class.java)
    controller = SearchController(orderService, auditService)
  }

  @Nested
  inner class SearchOrders {
    @Test
    fun `gets a query execution ID from order service`() {
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

      val queryExecutionId = "query-execution-id"

      val expectedResult = QueryExecutionResponse(
        queryExecutionId = queryExecutionId,
      )

      Mockito.`when`(orderService.getQueryExecutionId(orderSearchCriteria)).thenReturn(queryExecutionId)

      val result = controller.searchOrders(authentication, orderSearchCriteria)

      assertThat(result.body).isNotNull
      assertThat(result.body).isEqualTo(expectedResult)
    }

    @Test
    fun `passes on errors thrown when getting a query execution ID from order service`() {
      val orderSearchCriteria = OrderSearchCriteria(
        searchType = "name",
        legacySubjectId = "12345",
        firstName = "Martin",
        lastName = "Smythe",
        alias = null,
        dobDay = "02",
        dobMonth = "02",
        dobYear = "1975",
      )

      `when`(orderService.getQueryExecutionId(orderSearchCriteria, false)).thenThrow(RuntimeException::class.java)

      assertThrows<RuntimeException> { controller.searchOrders(authentication, orderSearchCriteria) }
    }
  }

  @Nested
  inner class SearchSpecialOrders {
    @Test
    fun `gets a specials query execution ID from order service`() {
      val specialsOrderSearchCriteria = OrderSearchCriteria(
        searchType = "name",
        legacySubjectId = "12345",
        firstName = "Martin",
        lastName = "Smythe",
        alias = null,
        dobDay = "02",
        dobMonth = "02",
        dobYear = "1975",
      )

      val queryExecutionId = "specials-query-execution-id"

      val expectedResult = QueryExecutionResponse(
        queryExecutionId = queryExecutionId,
      )

      `when`(orderService.getQueryExecutionId(specialsOrderSearchCriteria, true)).thenReturn(queryExecutionId)

      val result = controller.searchSpecialsOrders(authentication, specialsOrderSearchCriteria)

      assertThat(result.body).isNotNull
      assertThat(result.body).isEqualTo(expectedResult)
    }

    @Test
    fun `passes on errors thrown when getting a specials query execution ID from order service`() {
      val specialsOrderSearchCriteria = OrderSearchCriteria(
        searchType = "name",
        legacySubjectId = "12345",
        firstName = "Martin",
        lastName = "Smythe",
        alias = null,
        dobDay = "02",
        dobMonth = "02",
        dobYear = "1975",
      )

      `when`(orderService.getQueryExecutionId(specialsOrderSearchCriteria, true)).thenThrow(RuntimeException::class.java)

      assertThrows<RuntimeException> { controller.searchSpecialsOrders(authentication, specialsOrderSearchCriteria) }
    }
  }

  @Nested
  inner class GetOrderSearchResults {
    @Test
    fun `retrieves a list of orders from order service`() {
      val queryExecutionId = "query-execution-id"

      val expectedResult = listOf(
        OrderSearchResult(
          dataType = "am",
          legacySubjectId = 12345,
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

      Mockito.`when`(orderService.getSearchResults(queryExecutionId)).thenReturn(expectedResult)

      val result = controller.startSearch(authentication, queryExecutionId)

      assertThat(result.body).isNotNull
      assertThat(result.body).isEqualTo(expectedResult)
    }

    @Test
    fun `passes on errors thrown when retrieving a list of orders from order service`() {
      val queryExecutionId = "THROW ERROR"

      `when`(orderService.getSearchResults(queryExecutionId, false)).thenThrow(RuntimeException::class.java)

      assertThrows<RuntimeException> { controller.getOrdersSearchResults(authentication, queryExecutionId) }
    }
  }

  @Nested
  inner class GetSpecialOrdersSearchResults {
    @Test
    fun `retrieves a list of special orders from order service`() {
      val queryExecutionId = "specials-query-execution-id"

      val expectedResult = listOf(
        OrderSearchResult(
          dataType = "am",
          legacySubjectId = 12345,
          name = "Martin Smythe",
          addressLine1 = "First line of address",
          addressLine2 = "Second line of address",
          addressLine3 = "Third line of address",
          addressPostcode = "A0000A",
          alias = null,
          dateOfBirth = LocalDateTime.parse("1975-02-02T00:00:00"),
          orderStartDate = LocalDateTime.parse("2019-02-08T00:00:00"),
          orderEndDate = LocalDateTime.parse("2020-02-08T00:00:00"),
        ),
      )

      `when`(orderService.getSearchResults(queryExecutionId, true)).thenReturn(expectedResult)

      val result = controller.getSpecialOrdersSearchResults(authentication, queryExecutionId)

      assertThat(result.body).isNotNull
      assertThat(result.body).isEqualTo(expectedResult)
    }

    @Test
    fun `passes on errors thrown when retrieving a list of special orders from order service`() {
      val queryExecutionId = "THROW ERROR"

      `when`(orderService.getSearchResults(queryExecutionId, true)).thenThrow(RuntimeException::class.java)

      assertThrows<RuntimeException> { controller.getSpecialOrdersSearchResults(authentication, queryExecutionId) }
    }
  }
}
