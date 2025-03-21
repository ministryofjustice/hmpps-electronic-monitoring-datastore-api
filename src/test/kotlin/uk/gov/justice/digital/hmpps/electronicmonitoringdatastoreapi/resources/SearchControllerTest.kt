package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.security.core.Authentication
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.QueryExecutionResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.SearchController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class SearchControllerTest {
  private lateinit var orderService: OrderService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var controller: SearchController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = mock(Authentication::class.java)
    `when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    orderService = mock(OrderService::class.java)
    roleService = mock(AthenaRoleService::class.java)
    `when`(roleService.fromString(any<String>())).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    auditService = mock(AuditService::class.java)
    controller = SearchController(orderService, roleService, auditService)
  }

  @Nested
  inner class ConfirmAthenaAccess {
    @Test
    fun `calls AthenaRoleService for role and OrderService for checkAvailability`() {
      val expectedRole = AthenaRole.NONE

      `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(expectedRole)
      `when`(orderService.checkAvailability(AthenaRole.NONE)).thenReturn(true)
      `when`(authentication.principal).thenReturn("EXPECTED_PRINCIPAL")

      controller.confirmAthenaAccess(authentication)

      Mockito.verify(roleService, Mockito.times(1))
        .getRoleFromAuthentication(authentication)
      Mockito.verify(orderService, Mockito.times(1))
        .checkAvailability(expectedRole)
    }

    @Test
    fun `Returns true when ROLE_EM_DATASTORE_GENERAL_RO role found from authentication object`() {
      val expectedRole = AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO

      `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(expectedRole)
      `when`(orderService.checkAvailability(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(true)
      `when`(authentication.principal).thenReturn("EXPECTED_PRINCIPAL")

      val result = controller.confirmAthenaAccess(authentication)

      assertThat(result).isNotNull
      assertThat(result).isEqualTo(true)
    }

    @Test
    fun `Returns true when ROLE_EM_DATASTORE_RESTRICTED_RO role found from authentication object`() {
      val expectedRole = AthenaRole.ROLE_EM_DATASTORE_RESTRICTED_RO

      `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(expectedRole)
      `when`(orderService.checkAvailability(AthenaRole.ROLE_EM_DATASTORE_RESTRICTED_RO)).thenReturn(true)
      `when`(authentication.principal).thenReturn("EXPECTED_PRINCIPAL")

      val result = controller.confirmAthenaAccess(authentication)

      assertThat(result).isNotNull
      assertThat(result).isEqualTo(true)
    }

    @Test
    fun `Returns false when NONE role found from authentication object`() {
      val expectedRole = AthenaRole.NONE

      `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(expectedRole)
      `when`(orderService.checkAvailability(AthenaRole.NONE)).thenReturn(false)
      `when`(authentication.principal).thenReturn("EXPECTED_PRINCIPAL")

      val result = controller.confirmAthenaAccess(authentication)

      assertThat(result).isNotNull
      assertThat(result).isEqualTo(false)
    }
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
        dobDay = "01",
        dobMonth = "01",
        dobYear = "1970",
      )

      val queryExecutionId = "query-execution-id"

      val expectedResult = QueryExecutionResponse(
        queryExecutionId = queryExecutionId,
      )

      `when`(orderService.getQueryExecutionId(orderSearchCriteria, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(queryExecutionId)

      val result = controller.searchOrders(authentication, orderSearchCriteria)

      assertThat(result.body).isNotNull
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
          legacySubjectId = 12345,
          name = "Amy Smith",
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

      `when`(orderService.getSearchResults(queryExecutionId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = controller.getSearchResults(authentication, queryExecutionId)

      assertThat(result.body).isNotNull
      assertThat(result.body).isEqualTo(expectedResult)
    }
  }
}
