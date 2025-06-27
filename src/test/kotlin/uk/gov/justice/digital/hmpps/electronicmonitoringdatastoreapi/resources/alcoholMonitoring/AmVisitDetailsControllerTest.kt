package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources.alcoholMonitoring

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmVisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.alcoholMonitoring.AmVisitDetailsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring.AmVisitDetailsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class AmVisitDetailsControllerTest {
  private lateinit var amVisitDetailsService: AmVisitDetailsService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var amVisitDetailsController: AmVisitDetailsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    amVisitDetailsService = Mockito.mock(AmVisitDetailsService::class.java)
    roleService = Mockito.mock(AthenaRoleService::class.java)
    Mockito.`when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)
    auditService = Mockito.mock(AuditService::class.java)
    amVisitDetailsController = AmVisitDetailsController(amVisitDetailsService, auditService)
  }

  @Nested
  inner class GetVisitDetails {
    @Test
    fun `gets visit details from visit details service`() {
      val legacyOrderId = "1ab"
      val expectedResult = listOf(
        AmVisitDetails(
          legacySubjectId = "123",
          visitId = "300",
          visitType = "visit type",
          visitAttempt = "attempt 1",
          dateVisitRaised = LocalDateTime.parse("2001-01-01T00:00:00"),
          visitAddress = "test visit address",
          visitNotes = "visit notes",
          visitOutcome = "visit outcome",
          actualWorkStartDateTime = LocalDateTime.parse("2002-02-02T02:20:20"),
          actualWorkEndDateTime = LocalDateTime.parse("2003-03-03T03:30:30"),
          visitRejectionReason = "rejection reason",
          visitRejectionDescription = "rejection description",
          visitCancelReason = "cancel reason",
          visitCancelDescription = "cancel description",
        ),
      )

      Mockito.`when`(amVisitDetailsService.getVisitDetails(legacyOrderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)).thenReturn(expectedResult)

      val result = amVisitDetailsController.getVisitDetails(authentication, legacyOrderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(amVisitDetailsService, Mockito.times(1)).getVisitDetails(legacyOrderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)
    }
  }
}
