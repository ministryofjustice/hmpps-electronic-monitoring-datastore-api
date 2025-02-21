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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQueryResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaStringQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.SearchController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

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
    `when`(roleService.fromString(any<String>())).thenReturn(AthenaRole.DEV)
    `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.DEV)
    auditService = mock(AuditService::class.java)
    controller = SearchController(orderService, roleService, auditService)
  }

  @Nested
  inner class ConfirmAthenaAccess {
    @Test
    fun `calls AthenaRoleService for role and OrderService for checkAvailability`() {
      val expectedRole = AthenaRole.NONE

      `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(expectedRole)
      `when`(orderService.checkAthenaAccessible(AthenaRole.NONE)).thenReturn(true)
      `when`(authentication.principal).thenReturn("EXPECTED_PRINCIPAL")

      controller.confirmAthenaAccess(authentication)

      Mockito.verify(roleService, Mockito.times(1))
        .getRoleFromAuthentication(authentication)
      Mockito.verify(orderService, Mockito.times(1))
        .checkAthenaAccessible(expectedRole)
    }

    @Test
    fun `Returns true when ROLE_EM_DATASTORE_GENERAL_RO role found from authentication object`() {
      val expectedRole = AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO

      `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(expectedRole)
      `when`(orderService.checkAthenaAccessible(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(true)
      `when`(authentication.principal).thenReturn("EXPECTED_PRINCIPAL")

      val result = controller.confirmAthenaAccess(authentication)

      assertThat(result.body).isNotNull
      assertThat(result.body).isEqualTo(true)
    }

    @Test
    fun `Returns true when ROLE_EM_DATASTORE_RESTRICTED_RO role found from authentication object`() {
      val expectedRole = AthenaRole.ROLE_EM_DATASTORE_RESTRICTED_RO

      `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(expectedRole)
      `when`(orderService.checkAthenaAccessible(AthenaRole.ROLE_EM_DATASTORE_RESTRICTED_RO)).thenReturn(true)
      `when`(authentication.principal).thenReturn("EXPECTED_PRINCIPAL")

      val result = controller.confirmAthenaAccess(authentication)

      assertThat(result.body).isNotNull
      assertThat(result.body).isEqualTo(true)
    }

    @Test
    fun `Returns false when NONE role found from authentication object`() {
      val expectedRole = AthenaRole.NONE

      `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(expectedRole)
      `when`(orderService.checkAthenaAccessible(AthenaRole.NONE)).thenReturn(false)
      `when`(authentication.principal).thenReturn("EXPECTED_PRINCIPAL")

      val result = controller.confirmAthenaAccess(authentication)

      assertThat(result.body).isNotNull
      assertThat(result.body).isEqualTo(false)
    }
  }

  @Nested
  inner class QueryAthena {

    @Test
    fun `query orders from order service`() {
      val queryString = "fake query string"
      val queryRole = "fake-role"
      val queryObject = AthenaStringQuery(queryString)
      val queryResponse = "fake query response"

      `when`(orderService.runCustomQuery(queryObject, AthenaRole.DEV)).thenReturn(queryResponse)

      val result = controller.queryAthena(authentication, queryObject, queryRole)

      assertThat(result.body).isInstanceOf(AthenaQueryResponse::class.java)
      assertThat(result.body?.isErrored).isFalse
      assertThat(result.body?.queryString).isEqualTo(queryString)
      assertThat(result.body?.queryResponse).isEqualTo(queryResponse)
      assertThat(result.body?.errorMessage).isNullOrEmpty()
    }

    @Test
    fun `returns error response when orders service errors`() {
      val queryString = "fake query string"
      val queryRole = "fake-role"
      val queryObject = AthenaStringQuery(queryString)
      val errorMessage = "fake error message"

      `when`(orderService.runCustomQuery(queryObject, AthenaRole.DEV)).thenThrow(NullPointerException(errorMessage))

      val result = controller.queryAthena(authentication, queryObject, queryRole)

      assertThat(result.body).isInstanceOf(AthenaQueryResponse::class.java)
      assertThat(result.body?.isErrored).isTrue
      assertThat(result.body?.queryString).isEqualTo(queryString)
      assertThat(result.body?.errorMessage).isEqualTo(errorMessage)
    }
  }

  @Nested
  inner class SearchOrders {

    @Test
    fun `find list of orders from order service`() {
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

      val expectedResult = listOf(
        OrderSearchResult(
          dataType = "am",
          legacySubjectId = 12345,
          name = "Amy Smith",
          address = "First line of address",
          alias = null,
          dateOfBirth = "01-01-1970",
          orderStartDate = "08-02-2019",
          orderEndDate = "08-02-2020",
        ),
      )

      `when`(orderService.search(orderSearchCriteria, AthenaRole.DEV)).thenReturn(expectedResult)

      val result = controller.searchOrders(authentication, orderSearchCriteria)

      assertThat(result.body).isNotNull
      assertThat(result.body).isEqualTo(expectedResult)
    }
  }
}
