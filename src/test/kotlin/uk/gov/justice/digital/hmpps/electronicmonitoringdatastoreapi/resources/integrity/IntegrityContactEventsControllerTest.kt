package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources.integrity

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Event
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.integrity.IntegrityContactEventsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity.IntegrityOrderEventsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class IntegrityContactEventsControllerTest {
  private lateinit var integrityOrderEventsService: IntegrityOrderEventsService
  private lateinit var auditService: AuditService
  private lateinit var controller: IntegrityContactEventsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = mock(Authentication::class.java)
    `when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    integrityOrderEventsService = Mockito.mock(IntegrityOrderEventsService::class.java)
    auditService = Mockito.mock(AuditService::class.java)
    controller = IntegrityContactEventsController(integrityOrderEventsService, auditService)
  }

  @Nested
  inner class GetIntegrityGeneralContactEvents {
    @Test
    fun `gets contact events from integrity general contact events service`() {
      val legacySubjectId = "1ab"
      val expectedResult = listOf(
        Event(
          legacySubjectId = "1543",
          type = "TEST_STATUS",
          dateTime = LocalDateTime.of(2021, 1, 1, 1, 1, 1),
          details = IntegrityContactEventDetails(
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

      `when`(integrityOrderEventsService.getContactEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = controller.getGeneralContactEvents(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(integrityOrderEventsService, Mockito.times(1)).getContactEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }
  }

  @Nested
  inner class GetIntegrityRestrictedContactEvents {
    @Test
    fun `gets contact events from integrity restricted contact events service`() {
      val legacySubjectId = "1ab"
      val expectedResult = listOf(
        Event(
          legacySubjectId = "1543",
          type = "TEST_STATUS",
          dateTime = LocalDateTime.of(2021, 1, 1, 1, 1, 1),
          details = IntegrityContactEventDetails(
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

      `when`(integrityOrderEventsService.getContactEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_RESTRICTED_RO)).thenReturn(expectedResult)

      val result = controller.getRestrictedContactEvents(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(integrityOrderEventsService, Mockito.times(1)).getContactEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_RESTRICTED_RO)
    }
  }
}
