package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.IntegrityEvent
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.IntegrityMonitoringEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.IntegrityOrderEventsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class IntegrityMonitoringEventsControllerTest {
  private lateinit var integrityOrderEventsService: IntegrityOrderEventsService
  private lateinit var auditService: AuditService
  private lateinit var controller: IntegrityMonitoringEventsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    integrityOrderEventsService = Mockito.mock(IntegrityOrderEventsService::class.java)
    auditService = Mockito.mock(AuditService::class.java)
    controller = IntegrityMonitoringEventsController(integrityOrderEventsService, auditService)
  }

  @Nested
  inner class GetMonitoringEvents {
    @Test
    fun `gets monitoring events from order events service`() {
      val legacySubjectId = "1ab"
      val expectedResult = listOf(
        IntegrityEvent(
          legacySubjectId = "1543",
          type = "TEST_STATUS",
          dateTime = LocalDateTime.of(2021, 1, 1, 1, 1, 1),
          details = IntegrityMonitoringEventDetails(
            type = "monitoring",
            processedDateTime = LocalDateTime.of(2021, 1, 1, 1, 1, 2),
          ),
        ),
      )

      Mockito.`when`(integrityOrderEventsService.getMonitoringEvents(legacySubjectId, false)).thenReturn(expectedResult)

      val result = controller.getMonitoringEvents(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(integrityOrderEventsService, Mockito.times(1))
        .getMonitoringEvents(legacySubjectId, false)
    }
  }
}
