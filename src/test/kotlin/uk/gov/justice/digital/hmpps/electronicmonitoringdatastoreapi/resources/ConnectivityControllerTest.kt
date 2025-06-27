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
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.ConnectivityController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.IntegrityOrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@ActiveProfiles("test")
@JsonTest
class ConnectivityControllerTest {
  private lateinit var integrityOrderService: IntegrityOrderService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var controller: ConnectivityController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = mock(Authentication::class.java)
    `when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    integrityOrderService = mock(IntegrityOrderService::class.java)
    roleService = mock(AthenaRoleService::class.java)
    `when`(roleService.fromString(any<String>())).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)
    `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)
    auditService = mock(AuditService::class.java)
    controller = ConnectivityController(integrityOrderService, roleService, auditService)
  }

  @Nested
  inner class ConfirmAthenaAccess {
    @Test
    fun `calls AthenaRoleService for role and OrderService for checkAvailability`() {
      val expectedRole = AthenaRole.NONE

      `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(expectedRole)
      `when`(integrityOrderService.checkAvailability(AthenaRole.NONE)).thenReturn(true)
      `when`(authentication.principal).thenReturn("EXPECTED_PRINCIPAL")

      controller.test(authentication)

      Mockito.verify(roleService, Mockito.times(1))
        .getRoleFromAuthentication(authentication)
      Mockito.verify(integrityOrderService, Mockito.times(1))
        .checkAvailability(expectedRole)
    }

    @Test
    fun `Returns true when ROLE_EM_DATASTORE_GENERAL_RO role found from authentication object`() {
      val expectedRole = AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO

      `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(expectedRole)
      `when`(integrityOrderService.checkAvailability(AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)).thenReturn(true)
      `when`(authentication.principal).thenReturn("EXPECTED_PRINCIPAL")

      val result = controller.test(authentication)

      assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      assertThat(result.body).isEqualTo(mapOf("message" to "API Connection successful"))
    }

    @Test
    fun `Returns true when ROLE_EM_DATASTORE_RESTRICTED_RO role found from authentication object`() {
      val expectedRole = AthenaRole.ROLE_EM_DATASTORE_RESTRICTED__RO

      `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(expectedRole)
      `when`(integrityOrderService.checkAvailability(AthenaRole.ROLE_EM_DATASTORE_RESTRICTED__RO)).thenReturn(true)
      `when`(authentication.principal).thenReturn("EXPECTED_PRINCIPAL")

      val result = controller.test(authentication)

      assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      assertThat(result.body).isEqualTo(mapOf("message" to "API Connection successful"))
    }

    @Test
    fun `Returns false when NONE role found from authentication object`() {
      val expectedRole = AthenaRole.NONE

      `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(expectedRole)
      `when`(integrityOrderService.checkAvailability(AthenaRole.NONE)).thenReturn(false)
      `when`(authentication.principal).thenReturn("EXPECTED_PRINCIPAL")

      val result = controller.test(authentication)

      assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      assertThat(result.body).isEqualTo(mapOf("message" to "API Connection successful, but no access to Athena"))
    }
  }
}
