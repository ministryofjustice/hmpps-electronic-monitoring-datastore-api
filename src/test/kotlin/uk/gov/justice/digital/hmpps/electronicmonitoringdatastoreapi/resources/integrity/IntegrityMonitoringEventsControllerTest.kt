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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Event
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityMonitoringEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.integrity.MonitoringEventsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity.IntegrityOrderEventsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class IntegrityMonitoringEventsControllerTest {
  private lateinit var integrityOrderEventsService: IntegrityOrderEventsService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var controller: MonitoringEventsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    integrityOrderEventsService = Mockito.mock(IntegrityOrderEventsService::class.java)
    roleService = Mockito.mock(AthenaRoleService::class.java)
    Mockito.`when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    auditService = Mockito.mock(AuditService::class.java)
    controller = MonitoringEventsController(integrityOrderEventsService, roleService, auditService)
  }

  @Nested
  inner class GetMonitoringEvents {
    @Test
    fun `gets monitoring events from order events service`() {
      val legacySubjectId = "1ab"
      val expectedResult = listOf<Event<IntegrityMonitoringEventDetails>>(
        Event<IntegrityMonitoringEventDetails>(
          legacySubjectId = "1543",
          type = "TEST_STATUS",
          dateTime = LocalDateTime.of(2021, 1, 1, 1, 1, 1),
          details = IntegrityMonitoringEventDetails(
            type = "monitoring",
            processedDateTime = LocalDateTime.of(2021, 1, 1, 1, 1, 2),
          ),
        ),
      )

      Mockito.`when`(integrityOrderEventsService.getMonitoringEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = controller.getMonitoringEvents(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(integrityOrderEventsService, Mockito.times(1))
        .getMonitoringEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }
  }
}
