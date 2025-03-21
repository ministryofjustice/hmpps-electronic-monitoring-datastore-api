package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources.integrity

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.ConnectivityController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@ActiveProfiles("test")
@JsonTest
class ConnectivityControllerTest {
  private lateinit var orderService: OrderService
  private lateinit var auditService: AuditService
  private lateinit var controller: ConnectivityController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = mock(Authentication::class.java)
    `when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    orderService = mock(OrderService::class.java)
    auditService = mock(AuditService::class.java)
    controller = ConnectivityController(orderService, auditService)
  }

  @Nested
  inner class ConfirmAthenaAccess {
    @Test
    fun `calls OrderService to checkAvailability`() {
      `when`(orderService.checkAvailability()).thenReturn(true)
      `when`(authentication.principal).thenReturn("EXPECTED_PRINCIPAL")

      controller.test(authentication)

      Mockito.verify(orderService, Mockito.times(1)).checkAvailability()
    }

    @Test
    fun `Returns correct message when connectivity check is true`() {
      `when`(orderService.checkAvailability()).thenReturn(true)
      `when`(authentication.principal).thenReturn("EXPECTED_PRINCIPAL")

      val result = controller.test(authentication)

      assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      assertThat(result.body).isEqualTo(mapOf("message" to "API Connection successful"))
    }

    @Test
    fun `Returns correct message when connectivity check is false`() {
      `when`(orderService.checkAvailability()).thenReturn(false)
      `when`(authentication.principal).thenReturn("EXPECTED_PRINCIPAL")

      val result = controller.test(authentication)

      assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      assertThat(result.body).isEqualTo(mapOf("message" to "API Connection successful, but no access to Athena"))
    }

    @Test
    fun `Returns correct message when connectivity throws an error`() {
      `when`(orderService.checkAvailability()).thenThrow(RuntimeException::class.java)
      `when`(authentication.principal).thenReturn("EXPECTED_PRINCIPAL")

      val result = controller.test(authentication)

      assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      assertThat(result.body).isEqualTo(mapOf("message" to "Error determining Athena access"))
    }
  }
}
