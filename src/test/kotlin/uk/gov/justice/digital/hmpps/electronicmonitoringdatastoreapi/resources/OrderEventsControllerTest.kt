package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources

import org.assertj.core.api.Assertions
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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.ContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Event
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.IncidentEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.MonitoringEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.OrderEventsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderEventsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class OrderEventsControllerTest {
  private lateinit var orderEventsService: OrderEventsService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var controller: OrderEventsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = mock(Authentication::class.java)
    `when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    orderEventsService = Mockito.mock(OrderEventsService::class.java)
    roleService = mock(AthenaRoleService::class.java)
    `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.DEV)
    auditService = Mockito.mock(AuditService::class.java)
    controller = OrderEventsController(orderEventsService, roleService, auditService)
  }

  @Nested
  inner class GetMonitoringEvents {
    @Test
    fun `gets order information from order service`() {
      val orderId = "1ab"
      val expectedResult = listOf<Event<MonitoringEventDetails>>(
        Event<MonitoringEventDetails>(
          legacyOrderId = 123,
          legacySubjectId = 1543,
          type = "TEST_STATUS",
          dateTime = LocalDateTime.of(2021, 1, 1, 1, 1, 1),
          details = MonitoringEventDetails(
            processedDateTime = LocalDateTime.of(2021, 1, 1, 1, 1, 2),
          ),
        ),
      )

      `when`(orderEventsService.getMonitoringEvents(orderId, AthenaRole.DEV)).thenReturn(expectedResult)

      val result = controller.getMonitoringEvents(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(orderEventsService, Mockito.times(1)).getMonitoringEvents(orderId, AthenaRole.DEV)
    }
  }

  @Nested
  inner class GetViolationAlerts {
    @Test
    fun `gets order information from order service`() {
      val orderId = "1ab"
      val expectedResult = listOf<Event<IncidentEventDetails>>(
        Event<IncidentEventDetails>(
          legacyOrderId = 123,
          legacySubjectId = 1543,
          type = "TEST_STATUS",
          dateTime = LocalDateTime.of(2021, 1, 1, 1, 1, 1),
          details = IncidentEventDetails(
            violation = "TEST_VIOLATION",
          ),
        ),
      )

      `when`(orderEventsService.getIncidentEvents(orderId, AthenaRole.DEV)).thenReturn(expectedResult)

      val result = controller.getViolationAlerts(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(orderEventsService, Mockito.times(1)).getIncidentEvents(orderId, AthenaRole.DEV)
    }
  }

  @Nested
  inner class GetContactEvents {
    @Test
    fun `gets order information from order service`() {
      val orderId = "1ab"
      val expectedResult = listOf<Event<ContactEventDetails>>(
        Event<ContactEventDetails>(
          legacyOrderId = 123,
          legacySubjectId = 1543,
          type = "TEST_STATUS",
          dateTime = LocalDateTime.of(2021, 1, 1, 1, 1, 1),
          details = ContactEventDetails(
            outcome = null,
            contactType = "PHONE_CALL",
            reason = "TEST_REASON",
            channel = "TEST_CHANNEL",
            userId = null,
            userName = null,
            modifiedDateTime = LocalDateTime.of(2019, 9, 1, 12, 15, 9),
          ),
        ),
      )

      `when`(orderEventsService.getContactEvents(orderId, AthenaRole.DEV)).thenReturn(expectedResult)

      val result = controller.getContactEvents(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(orderEventsService, Mockito.times(1)).getContactEvents(orderId, AthenaRole.DEV)
    }
  }
}
