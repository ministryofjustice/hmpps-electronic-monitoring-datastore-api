package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources.integrity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Event
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IncidentEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.integrity.IntegrityIncidentEventsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity.IntegrityOrderEventsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class IntegrityIncidentEventsControllerTest {
  private lateinit var integrityOrderEventsService: IntegrityOrderEventsService
  private lateinit var auditService: AuditService
  private lateinit var controller: IntegrityIncidentEventsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    integrityOrderEventsService = Mockito.mock(IntegrityOrderEventsService::class.java)
    auditService = Mockito.mock(AuditService::class.java)
    controller = IntegrityIncidentEventsController(integrityOrderEventsService, auditService)
  }

  @Nested
  inner class GetIncidentEvents {
    @Test
    fun `gets incident events from order events service`() {
      val legacySubjectId = "1ab"
      val expectedResult = listOf(
        Event(
          legacySubjectId = "1543",
          type = "TEST_STATUS",
          dateTime = LocalDateTime.of(2021, 1, 1, 1, 1, 1),
          details = IncidentEventDetails(
            type = "incident",
          ),
        ),
      )

      Mockito.`when`(integrityOrderEventsService.getIncidentEvents(legacySubjectId, false)).thenReturn(expectedResult)

      val result = controller.getIncidentEvents(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(integrityOrderEventsService, Mockito.times(1))
        .getIncidentEvents(legacySubjectId, false)
    }
  }
}
