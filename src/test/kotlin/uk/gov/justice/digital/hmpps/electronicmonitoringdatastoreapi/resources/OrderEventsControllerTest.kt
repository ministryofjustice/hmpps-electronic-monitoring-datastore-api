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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.ViolationEventDetails
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
    `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    auditService = Mockito.mock(AuditService::class.java)
    controller = OrderEventsController(orderEventsService, roleService, auditService)
  }

  @Nested
  inner class GetIncidentEvents {
    @Test
    fun `gets incident events from order events service`() {
      val orderId = "1ab"
      val expectedResult = listOf<Event<IncidentEventDetails>>(
        Event<IncidentEventDetails>(
          legacyOrderId = 123,
          legacySubjectId = 1543,
          type = "TEST_STATUS",
          dateTime = LocalDateTime.of(2021, 1, 1, 1, 1, 1),
          details = IncidentEventDetails(
            type = "incident",
          ),
        ),
      )

      `when`(orderEventsService.getIncidentEvents(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = controller.getIncidentEvents(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(orderEventsService, Mockito.times(1)).getIncidentEvents(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }
  }

  @Nested
  inner class GetViolationEvents {
    @Test
    fun `gets violation events from order events service`() {
      val orderId = "1ab"
      val expectedResult = listOf<Event<ViolationEventDetails>>(
        Event<ViolationEventDetails>(
          legacyOrderId = 123,
          legacySubjectId = 1543,
          type = "TEST_STATUS",
          dateTime = LocalDateTime.of(2021, 1, 1, 1, 1, 1),
          details = ViolationEventDetails(
            enforcementReason = "ENFORCEMENT_REASON",
            investigationOutcomeReason = "INVESTIGATION_OUTCOME",
            breachDetails = "BREACH_DETAILS",
            breachEnforcementOutcome = "ENFORCEMENT_OUTCOME",
            agencyAction = "AGENCY_ACTION",
            breachDateTime = LocalDateTime.parse("2020-01-01T00:00:00"),
            breachIdentifiedDateTime = LocalDateTime.parse("2020-01-01T00:00:00"),
            authorityFirstNotifiedDateTime = LocalDateTime.parse("2020-01-01T00:00:00"),
            agencyResponseDate = LocalDateTime.parse("2020-01-01T00:00:00"),
            breachPackRequestedDate = LocalDateTime.parse("2020-01-01T00:00:00"),
            breachPackSentDate = LocalDateTime.parse("2020-01-01T00:00:00"),
            section9Date = LocalDateTime.parse("2020-01-01T00:00:00"),
            hearingDate = LocalDateTime.parse("2020-01-01T00:00:00"),
            summonsServedDate = LocalDateTime.parse("2020-01-01T00:00:00"),
            subjectLetterSentDate = LocalDateTime.parse("2020-01-01T00:00:00"),
            warningLetterSentDateTime = LocalDateTime.parse("2020-01-01T00:00:00"),
          ),
        ),
      )

      `when`(orderEventsService.getViolationEvents(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = controller.getViolationEvents(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(orderEventsService, Mockito.times(1)).getViolationEvents(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }
  }

  @Nested
  inner class GetContactEvents {
    @Test
    fun `gets contact events from order events service`() {
      val orderId = "1ab"
      val expectedResult = listOf<Event<ContactEventDetails>>(
        Event<ContactEventDetails>(
          legacyOrderId = 123,
          legacySubjectId = 1543,
          type = "TEST_STATUS",
          dateTime = LocalDateTime.of(2021, 1, 1, 1, 1, 1),
          details = ContactEventDetails(
            outcome = null,
            type = "PHONE_CALL",
            reason = "TEST_REASON",
            channel = "TEST_CHANNEL",
            userId = null,
            userName = null,
            modifiedDateTime = LocalDateTime.of(2019, 9, 1, 12, 15, 9),
          ),
        ),
      )

      `when`(orderEventsService.getContactEvents(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = controller.getContactEvents(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(orderEventsService, Mockito.times(1)).getContactEvents(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }
  }
}
