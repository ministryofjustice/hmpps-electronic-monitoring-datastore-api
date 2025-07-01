package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources.integrity

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
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_GENERAL__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_RESTRICTED__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.QueryExecutionResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.integrity.IntegrityOrderDetailsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity.IntegrityOrderDetailsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDate
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class IntegrityOrderDetailsControllerTest {
  private lateinit var orderDetailsService: IntegrityOrderDetailsService
  private lateinit var auditService: AuditService
  private lateinit var orderDetailsController: IntegrityOrderDetailsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = mock<Authentication>()
    whenever(authentication.name).thenReturn("MOCK_AUTH_USER")
    orderDetailsService = mock<IntegrityOrderDetailsService>()
    auditService = mock<AuditService>()
    orderDetailsController = IntegrityOrderDetailsController(orderDetailsService, auditService)
  }

  fun integrityOrderDetails(specials: String): IntegrityOrderDetails = IntegrityOrderDetails(
    specials = specials,
    legacySubjectId = "AA12345",
    legacyOrderId = "1234567",
    firstName = "John",
    lastName = "Smith",
    alias = "Zeno",
    dateOfBirth = LocalDateTime.parse("1980-02-01T00:00:00"),
    adultOrChild = "adult",
    sex = "Sex",
    contact = "contact",
    primaryAddressLine1 = "1 Primary Street",
    primaryAddressLine2 = "Sutton",
    primaryAddressLine3 = "London",
    primaryAddressPostCode = "ABC 123",
    phoneOrMobileNumber = "09876543210",
    offenceRisk = false,
    postCodeRisk = "",
    falseLimbRisk = "",
    migratedRisk = "",
    rangeRisk = "",
    reportRisk = "",
    orderStartDate = LocalDateTime.parse("2012-02-01T00:00:00"),
    orderEndDate = LocalDateTime.parse("2013-04-03T00:00:00"),
    orderType = "Community",
    orderTypeDescription = "",
    orderTypeDetail = "",
    wearingWristPid = "",
    notifyingOrganisationDetailsName = "",
    responsibleOrganisation = "",
    responsibleOrganisationDetailsRegion = "",
  )

  @Nested
  inner class GetIntegrityGeneralOrderDetails {
    @BeforeEach
    fun setup() {
      whenever(authentication.authorities).thenReturn(
        listOf(
          SimpleGrantedAuthority(ROLE_EM_DATASTORE_GENERAL__RO),
        ),
      )
    }

    @Test
    fun `gets general order details from order details service`() {
      val legacySubjectId = "2gt"
      val expectedResult = integrityOrderDetails("no")

      whenever(orderDetailsService.getOrderDetails(legacySubjectId, false)).thenReturn(expectedResult)

      val result = orderDetailsController.getOrderDetails(authentication, legacySubjectId)

      assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      assertThat(result.body).isEqualTo(expectedResult)
      verify(orderDetailsService, times(1)).getOrderDetails(legacySubjectId, false)
    }
  }

  @Nested
  inner class GetIntegrityRestrictedOrderDetails {
    @BeforeEach
    fun setup() {
      whenever(authentication.authorities).thenReturn(
        listOf(
          SimpleGrantedAuthority(ROLE_EM_DATASTORE_RESTRICTED__RO),
        ),
      )
    }

    @Test
    fun `gets restricted order details from order details service`() {
      val legacySubjectId = "2gt"
      val expectedResult = integrityOrderDetails("yes")

      whenever(orderDetailsService.getOrderDetails(legacySubjectId, true)).thenReturn(expectedResult)

      val result = orderDetailsController.getOrderDetails(authentication, legacySubjectId, true)

      assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      assertThat(result.body).isEqualTo(expectedResult)
      verify(orderDetailsService, times(1)).getOrderDetails(legacySubjectId, true)
    }
  }

  @Nested
  inner class SearchOrders {
    @Test
    fun `get a query execution ID from order service`() {
      val orderSearchCriteria = OrderSearchCriteria(
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

      whenever(orderDetailsService.getQueryExecutionId(orderSearchCriteria, false)).thenReturn(queryExecutionId)

      val result = orderDetailsController.executeSearch(authentication, orderSearchCriteria)

      assertThat(result.body).isEqualTo(expectedResult)
    }
  }

  @Nested
  inner class GetSearchResult {
    @Test
    fun `find list of orders from order service`() {
      val queryExecutionId = "query-execution-id"

      val expectedResult = listOf(
        integrityOrderDetails("no"),
        integrityOrderDetails("yes"),
      )

      whenever(orderDetailsService.getSearchResults(queryExecutionId, false)).thenReturn(expectedResult)

      val result = orderDetailsController.getSearchResults(authentication, queryExecutionId)

      assertThat(result.body).isEqualTo(expectedResult)
    }
  }
}
