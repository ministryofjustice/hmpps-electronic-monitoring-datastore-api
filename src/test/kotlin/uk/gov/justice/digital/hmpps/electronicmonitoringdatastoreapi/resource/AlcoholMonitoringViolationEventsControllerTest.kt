package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AlcoholMonitoringEvent
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AlcoholMonitoringViolationEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AlcoholMonitoringOrderEventsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class AlcoholMonitoringViolationEventsControllerTest {
  private lateinit var alcoholMonitoringOrderEventsService: AlcoholMonitoringOrderEventsService
  private lateinit var auditService: AuditService
  private lateinit var controller: AlcoholMonitoringViolationEventsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = mock(Authentication::class.java)
    `when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    alcoholMonitoringOrderEventsService = Mockito.mock(AlcoholMonitoringOrderEventsService::class.java)
    auditService = Mockito.mock(AuditService::class.java)
    controller = AlcoholMonitoringViolationEventsController(alcoholMonitoringOrderEventsService, auditService)
  }

  @Nested
  inner class GetViolationEvents {
    @Test
    fun `gets violation events from alcohol monitoring order events service`() {
      val legacySubjectId = "1ab"
      val expectedResult = listOf(
        AlcoholMonitoringEvent(
          legacySubjectId = "1543",
          type = "TEST_STATUS",
          dateTime = LocalDateTime.of(2001, 1, 1, 1, 1, 1),
          details = AlcoholMonitoringViolationEventDetails(
            enforcementId = "E001",
            nonComplianceReason = "Test noncompliance reason",
            nonComplianceDateTime = LocalDateTime.of(2002, 2, 2, 2, 2, 2),
            violationAlertId = "V001",
            violationAlertDescription = "Test alert description",
            violationEventNotificationDateTime = LocalDateTime.of(2003, 3, 3, 3, 3, 3),
            actionTakenEms = "Test action taken EMS",
            nonComplianceOutcome = "Test outcome",
            nonComplianceResolved = "Yes",
            dateResolved = LocalDateTime.of(2004, 4, 4, 4, 4, 4),
            openClosed = "Closed",
            visitRequired = "No",
          ),
        ),
      )

      `when`(alcoholMonitoringOrderEventsService.getViolationEvents(legacySubjectId)).thenReturn(expectedResult)

      val result = controller.getViolationEvents(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(alcoholMonitoringOrderEventsService, Mockito.times(1)).getViolationEvents(legacySubjectId)
    }
  }
}
