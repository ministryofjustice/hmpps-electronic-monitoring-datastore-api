package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources.alcoholMonitoring

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.QueryExecutionResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AlcoholMonitoringOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring.AlcoholMonitoringOrderDetailsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.internal.AuditService
import java.time.LocalDate
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class AlcoholMonitoringOrderDetailsControllerTest {
  private lateinit var alcoholMonitoringOrderDetailsService: AlcoholMonitoringOrderDetailsService
  private lateinit var auditService: AuditService
  private lateinit var alcoholMonitoringOrderDetailsController: AlcoholMonitoringOrderDetailsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = mock<Authentication>()
    whenever(authentication.name).thenReturn("MOCK_AUTH_USER")
    alcoholMonitoringOrderDetailsService = mock<AlcoholMonitoringOrderDetailsService>()
    auditService = mock<AuditService>()
    alcoholMonitoringOrderDetailsController = AlcoholMonitoringOrderDetailsController(alcoholMonitoringOrderDetailsService, auditService)
  }

  val expectedResult = AlcoholMonitoringOrderDetails(
    legacySubjectId = "AA12345",
    firstName = "John",
    lastName = "Smith",
    alias = "Zeno",
    dateOfBirth = LocalDateTime.parse("1980-02-01T00:00:00"),
    sex = "Sex",
    specialInstructions = "Special instructions",
    phoneNumber = "09876543210",
    address1 = "1 Primary Street",
    address2 = "Sutton",
    address3 = "London",
    postcode = "ABC 123",
    legacyOrderId = "1234567",
    orderStartDate = LocalDateTime.parse("2012-02-01T00:00:00"),
    orderEndDate = LocalDateTime.parse("2013-04-03T00:00:00"),
    enforceableCondition = "Enforceable condition",
    orderType = "Community",
    orderTypeDescription = "",
    orderEndOutcome = "",
    responsibleOrganisationPhoneNumber = "01234567890",
    responsibleOrganisationEmail = "a@b.c",
    tagAtSource = "",
  )

  @Nested
  inner class GetAlcoholMonitoringOrderDetails {
    @Test
    fun `gets order details from order details service`() {
      val legacySubjectId = "2gt"

      whenever(alcoholMonitoringOrderDetailsService.getOrderDetails(legacySubjectId)).thenReturn(expectedResult)

      val result = alcoholMonitoringOrderDetailsController.getOrderDetails(authentication, legacySubjectId)

      assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      assertThat(result.body).isEqualTo(expectedResult)
      verify(alcoholMonitoringOrderDetailsService, times(1)).getOrderDetails(legacySubjectId)
    }
  }

  @Nested
  inner class SearchOrders {
    @Test
    fun `get a query execution ID from order service`() {
      val orderSearchCriteria = OrderSearchCriteria(
        legacySubjectId = "12345",
        firstName = "Pheobe",
        lastName = "Smith",
        alias = null,
        dateOfBirth = LocalDate.parse("1970-01-01"),
      )

      val queryExecutionId = "query-execution-id"

      val expectedResult = QueryExecutionResponse(
        queryExecutionId = queryExecutionId,
      )

      whenever(alcoholMonitoringOrderDetailsService.getQueryExecutionId(orderSearchCriteria, false)).thenReturn(queryExecutionId)

      val result = alcoholMonitoringOrderDetailsController.executeSearch(authentication, orderSearchCriteria)

      assertThat(result.body).isEqualTo(expectedResult)
    }
  }

  @Nested
  inner class GetSearchResult {
    @Test
    fun `find list of orders from order service`() {
      val queryExecutionId = "query-execution-id"

      val expectedResult = listOf(
        expectedResult,
      )

      whenever(alcoholMonitoringOrderDetailsService.getSearchResults(queryExecutionId, false)).thenReturn(expectedResult)

      val result = alcoholMonitoringOrderDetailsController.getSearchResults(authentication, queryExecutionId)

      assertThat(result.body).isNotEmpty
      assertThat(result.body).isEqualTo(expectedResult)
    }
  }
}
