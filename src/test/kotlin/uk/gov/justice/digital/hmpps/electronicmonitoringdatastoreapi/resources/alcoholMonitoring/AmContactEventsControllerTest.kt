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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmEvent
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.alcoholMonitoring.AmContactEventsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring.AmOrderEventsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime
import java.util.UUID

@ActiveProfiles("test")
@JsonTest
class AmContactEventsControllerTest {
  private lateinit var amOrderEventsService: AmOrderEventsService
  private lateinit var auditService: AuditService
  private lateinit var controller: AmContactEventsController
  private lateinit var authentication: Authentication

  private lateinit var legacySubjectId: String

  @BeforeEach
  fun setup() {
    authentication = mock(Authentication::class.java)
    `when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    amOrderEventsService = Mockito.mock(AmOrderEventsService::class.java)
    auditService = Mockito.mock(AuditService::class.java)
    controller = AmContactEventsController(amOrderEventsService, auditService)

    legacySubjectId = UUID.randomUUID().toString()
  }

  @Nested
  inner class GetContactEvents {
    val expectedEmptyResult = emptyList<AmEvent<AmContactEventDetails>>()

    @Test
    fun `Calls service`() {
      `when`(amOrderEventsService.getContactEvents(legacySubjectId)).thenReturn(expectedEmptyResult)

      controller.getContactEvents(authentication, legacySubjectId)

      Mockito.verify(amOrderEventsService, Mockito.times(1)).getContactEvents(legacySubjectId)
    }

    @Test
    fun `gets empty list from service`() {
      `when`(amOrderEventsService.getContactEvents(legacySubjectId)).thenReturn(expectedEmptyResult)

      val result = controller.getContactEvents(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedEmptyResult)
    }

    @Test
    fun `gets list of results from service`() {
      val expectedResult = listOf(
        AmEvent(
          legacySubjectId = legacySubjectId,
          type = "TEST_STATUS",
          dateTime = LocalDateTime.of(2001, 1, 1, 1, 1, 1),
          details = AmContactEventDetails(
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

      `when`(amOrderEventsService.getContactEvents(legacySubjectId)).thenReturn(expectedResult)

      val result = controller.getContactEvents(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)
    }

    @Test
    fun `Propagates errors from service when it throws an error`() {
      val expectedError = RuntimeException("Fake error form service")

      `when`(amOrderEventsService.getContactEvents(legacySubjectId)).thenThrow(expectedError)

      Assertions.assertThatThrownBy { controller.getContactEvents(authentication, legacySubjectId) }.isInstanceOf(RuntimeException::class.java)

      Mockito.verify(amOrderEventsService, Mockito.times(1)).getContactEvents(legacySubjectId)
    }
  }
}
