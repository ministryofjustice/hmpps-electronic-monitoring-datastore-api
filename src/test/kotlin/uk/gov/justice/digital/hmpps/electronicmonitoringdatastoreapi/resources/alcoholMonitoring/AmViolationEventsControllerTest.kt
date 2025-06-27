package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources.alcoholMonitoring

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmEvent
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmViolationEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.alcoholMonitoring.AmViolationEventsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring.AmOrderEventsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class AmViolationEventsControllerTest {
  private lateinit var amOrderEventsService: AmOrderEventsService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var controller: AmViolationEventsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = mock(Authentication::class.java)
    `when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    amOrderEventsService = Mockito.mock(AmOrderEventsService::class.java)
    roleService = mock(AthenaRoleService::class.java)
    `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)
    auditService = Mockito.mock(AuditService::class.java)
    controller = AmViolationEventsController(amOrderEventsService, auditService)
  }

  @Nested
  inner class GetViolationEvents {
    @Test
    fun `gets violation events from alcohol monitoring order events service`() {
      val legacySubjectId = "1ab"
      val expectedResult = listOf(
        AmEvent(
          legacySubjectId = "1543",
          type = "TEST_STATUS",
          dateTime = LocalDateTime.of(2001, 1, 1, 1, 1, 1),
          details = AmViolationEventDetails(
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

      `when`(amOrderEventsService.getViolationEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)).thenReturn(expectedResult)

      val result = controller.getViolationEvents(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(amOrderEventsService, Mockito.times(1)).getViolationEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)
    }
  }
}
