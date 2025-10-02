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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.AlcoholMonitoringVisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AlcoholMonitoringVisitDetailsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class AlcoholMonitoringVisitDetailsControllerTest {
  private lateinit var alcoholMonitoringVisitDetailsService: AlcoholMonitoringVisitDetailsService
  private lateinit var auditService: AuditService
  private lateinit var alcoholMonitoringVisitDetailsController: AlcoholMonitoringVisitDetailsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    alcoholMonitoringVisitDetailsService = Mockito.mock(AlcoholMonitoringVisitDetailsService::class.java)
    auditService = Mockito.mock(AuditService::class.java)
    alcoholMonitoringVisitDetailsController =
      AlcoholMonitoringVisitDetailsController(alcoholMonitoringVisitDetailsService, auditService)
  }

  @Nested
  inner class GetVisitDetails {
    @Test
    fun `gets visit details from visit details service`() {
      val legacyOrderId = "1ab"
      val expectedResult = listOf(
        AlcoholMonitoringVisitDetails(
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

      Mockito.`when`(alcoholMonitoringVisitDetailsService.getVisitDetails(legacyOrderId)).thenReturn(expectedResult)

      val result = alcoholMonitoringVisitDetailsController.getVisitDetails(authentication, legacyOrderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(alcoholMonitoringVisitDetailsService, Mockito.times(1)).getVisitDetails(legacyOrderId)
    }
  }
}
