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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AmEvent
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AmIncidentEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring.AmOrderEventsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class AmIncidentEventsControllerTest {
  private lateinit var amOrderEventsService: AmOrderEventsService
  private lateinit var auditService: AuditService
  private lateinit var controller: AmIncidentEventsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = mock(Authentication::class.java)
    `when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    amOrderEventsService = Mockito.mock(AmOrderEventsService::class.java)
    auditService = Mockito.mock(AuditService::class.java)
    controller = AmIncidentEventsController(amOrderEventsService, auditService)
  }

  @Nested
  inner class GetIncidentEvents {
    @Test
    fun `gets incident events from alcohol monitoring order events service`() {
      val legacySubjectId = "1ab"
      val expectedResult = listOf(
        AmEvent(
          legacySubjectId = "1543",
          type = "TEST_STATUS",
          dateTime = LocalDateTime.of(2001, 1, 1, 1, 1, 1),
          details = AmIncidentEventDetails(
            violationAlertId = "V001",
            violationAlertDateTime = LocalDateTime.of(2002, 2, 2, 2, 2, 2),
            violationAlertType = "Test alert type",
            violationAlertResponseAction = "Test response action",
            visitRequired = "No",
            probationInteractionRequired = "No",
            amsInteractionRequired = "Yes",
            multipleAlerts = "Yes",
            additionalAlerts = "Test additional alerts",
          ),
        ),
      )

      `when`(amOrderEventsService.getIncidentEvents(legacySubjectId)).thenReturn(expectedResult)

      val result = controller.getIncidentEvents(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(amOrderEventsService, Mockito.times(1)).getIncidentEvents(legacySubjectId)
    }
  }
}
