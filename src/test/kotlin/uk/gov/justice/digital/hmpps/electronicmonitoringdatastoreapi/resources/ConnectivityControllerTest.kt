package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_GENERAL__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_RESTRICTED__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.AvailabilityService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.internal.AuditService

@ActiveProfiles("test")
@JsonTest
class ConnectivityControllerTest {
  private lateinit var availabilityService: AvailabilityService
  private lateinit var auditService: AuditService
  private lateinit var controller: ConnectivityController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = mock(Authentication::class.java)
    `when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    availabilityService = mock(AvailabilityService::class.java)
    auditService = mock(AuditService::class.java)
    controller = ConnectivityController(availabilityService, auditService)
  }

  @Nested
  inner class ConfirmGeneralAthenaAccess {
    @Test
    fun `Returns success message when check returns true`() {
      `when`(availabilityService.checkAvailability(false)).thenReturn(true)
      `when`(authentication.principal).thenReturn(ROLE_EM_DATASTORE_GENERAL__RO)

      val result = controller.test(authentication)

      Mockito.verify(availabilityService, Mockito.times(1)).checkAvailability(false)

      assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      assertThat(result.body).isEqualTo(mapOf("message" to "API Connection successful"))
    }

    @Test
    fun `Returns failure message when check returns false`() {
      `when`(availabilityService.checkAvailability(false)).thenReturn(false)
      `when`(authentication.principal).thenReturn(ROLE_EM_DATASTORE_GENERAL__RO)

      val result = controller.test(authentication)

      Mockito.verify(availabilityService, Mockito.times(1)).checkAvailability(false)

      assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      assertThat(result.body).isEqualTo(mapOf("message" to "API Connection successful, but no access to Athena"))
    }

    @Test
    fun `Returns error message when check throws an error`() {
      `when`(availabilityService.checkAvailability(false)).thenThrow(RuntimeException())
      `when`(authentication.principal).thenReturn(ROLE_EM_DATASTORE_GENERAL__RO)

      val result = controller.test(authentication)

      Mockito.verify(availabilityService, Mockito.times(1)).checkAvailability(false)

      assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      assertThat(result.body).isEqualTo(mapOf("message" to "Error determining Athena access"))
    }
  }

  @Nested
  inner class ConfirmRestrictedAthenaAccess {
    @Test
    fun `Returns success message when check returns true`() {
      `when`(availabilityService.checkAvailability(true)).thenReturn(true)
      `when`(authentication.principal).thenReturn(ROLE_EM_DATASTORE_RESTRICTED__RO)

      val result = controller.test(authentication, true)

      Mockito.verify(availabilityService, Mockito.times(1)).checkAvailability(true)

      assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      assertThat(result.body).isEqualTo(mapOf("message" to "API Connection successful"))
    }

    @Test
    fun `Returns failure message when check returns false`() {
      `when`(availabilityService.checkAvailability(true)).thenReturn(false)
      `when`(authentication.principal).thenReturn(ROLE_EM_DATASTORE_RESTRICTED__RO)

      val result = controller.test(authentication, true)

      Mockito.verify(availabilityService, Mockito.times(1)).checkAvailability(true)

      assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      assertThat(result.body).isEqualTo(mapOf("message" to "API Connection successful, but no access to Athena"))
    }

    @Test
    fun `Returns error message when check throws an error`() {
      `when`(availabilityService.checkAvailability(true)).thenThrow(RuntimeException())
      `when`(authentication.principal).thenReturn(ROLE_EM_DATASTORE_RESTRICTED__RO)

      val result = controller.test(authentication, true)

      Mockito.verify(availabilityService, Mockito.times(1)).checkAvailability(true)

      assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      assertThat(result.body).isEqualTo(mapOf("message" to "Error determining Athena access"))
    }
  }
}
