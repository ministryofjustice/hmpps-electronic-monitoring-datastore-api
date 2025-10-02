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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.AlcoholMonitoringContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.AlcoholMonitoringEvent
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AlcoholMonitoringOrderEventsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime
import java.util.UUID

@ActiveProfiles("test")
@JsonTest
class AlcoholMonitoringContactEventsControllerTest {
  private lateinit var alcoholMonitoringOrderEventsService: AlcoholMonitoringOrderEventsService
  private lateinit var auditService: AuditService
  private lateinit var controller: AlcoholMonitoringContactEventsController
  private lateinit var authentication: Authentication

  private lateinit var legacySubjectId: String

  @BeforeEach
  fun setup() {
    authentication = mock(Authentication::class.java)
    `when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    alcoholMonitoringOrderEventsService = Mockito.mock(AlcoholMonitoringOrderEventsService::class.java)
    auditService = Mockito.mock(AuditService::class.java)
    controller = AlcoholMonitoringContactEventsController(alcoholMonitoringOrderEventsService, auditService)

    legacySubjectId = UUID.randomUUID().toString()
  }

  @Nested
  inner class GetContactEvents {
    val expectedEmptyResult = emptyList<AlcoholMonitoringEvent<AlcoholMonitoringContactEventDetails>>()

    @Test
    fun `Calls service`() {
      `when`(alcoholMonitoringOrderEventsService.getContactEvents(legacySubjectId)).thenReturn(expectedEmptyResult)

      controller.getContactEvents(authentication, legacySubjectId)

      Mockito.verify(alcoholMonitoringOrderEventsService, Mockito.times(1)).getContactEvents(legacySubjectId)
    }

    @Test
    fun `gets empty list from service`() {
      `when`(alcoholMonitoringOrderEventsService.getContactEvents(legacySubjectId)).thenReturn(expectedEmptyResult)

      val result = controller.getContactEvents(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedEmptyResult)
    }

    @Test
    fun `gets list of results from service`() {
      val expectedResult = listOf(
        AlcoholMonitoringEvent(
          legacySubjectId = legacySubjectId,
          type = "TEST_STATUS",
          dateTime = LocalDateTime.of(2001, 1, 1, 1, 1, 1),
          details = AlcoholMonitoringContactEventDetails(
            contactDateTime = LocalDateTime.of(2002, 2, 2, 2, 2, 2),
            inboundOrOutbound = "Inbound",
            fromTo = "From",
            channel = "Probation",
            subjectConsentWithdrawn = "No",
            callOutcome = "Test call outcome",
            statement = "Test statement",
            reasonForContact = "Test contact reason",
            outcomeOfContact = "Test contact outcome",
            visitRequired = "No ",
            visitId = "V001",
          ),
        ),
      )

      `when`(alcoholMonitoringOrderEventsService.getContactEvents(legacySubjectId)).thenReturn(expectedResult)

      val result = controller.getContactEvents(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)
    }

    @Test
    fun `Propagates errors from service when it throws an error`() {
      val expectedError = RuntimeException("Fake error form service")

      `when`(alcoholMonitoringOrderEventsService.getContactEvents(legacySubjectId)).thenThrow(expectedError)

      Assertions.assertThatThrownBy { controller.getContactEvents(authentication, legacySubjectId) }.isInstanceOf(RuntimeException::class.java)

      Mockito.verify(alcoholMonitoringOrderEventsService, Mockito.times(1)).getContactEvents(legacySubjectId)
    }
  }
}
