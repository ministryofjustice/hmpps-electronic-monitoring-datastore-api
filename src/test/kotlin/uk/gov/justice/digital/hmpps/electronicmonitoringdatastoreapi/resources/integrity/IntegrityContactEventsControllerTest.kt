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
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_GENERAL__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_RESTRICTED__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.Event
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.integrity.IntegrityContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.integrity.IntegrityOrderEventsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.internal.AuditService
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
    @BeforeEach
    fun setup() {
      `when`(authentication.authorities).thenReturn(
        listOf(
          SimpleGrantedAuthority(ROLE_EM_DATASTORE_GENERAL__RO),
        ),
      )
    }

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

      `when`(integrityOrderEventsService.getContactEvents(legacySubjectId, false)).thenReturn(expectedResult)

      val result = controller.getContactEvents(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(integrityOrderEventsService, Mockito.times(1)).getContactEvents(legacySubjectId, false)
    }
  }

  @Nested
  inner class GetIntegrityRestrictedContactEvents {
    @BeforeEach
    fun setup() {
      `when`(authentication.authorities).thenReturn(
        listOf(
          SimpleGrantedAuthority(ROLE_EM_DATASTORE_RESTRICTED__RO),
        ),
      )
    }

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

      `when`(integrityOrderEventsService.getContactEvents(legacySubjectId, true)).thenReturn(expectedResult)

      val result = controller.getContactEvents(authentication, legacySubjectId, true)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(integrityOrderEventsService, Mockito.times(1)).getContactEvents(legacySubjectId, true)
    }
  }
}
