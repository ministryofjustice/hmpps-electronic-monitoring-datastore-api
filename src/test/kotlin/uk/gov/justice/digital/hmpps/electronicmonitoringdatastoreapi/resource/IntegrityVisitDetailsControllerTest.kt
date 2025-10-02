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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.IntegrityVisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.IntegrityVisitDetailsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class IntegrityVisitDetailsControllerTest {
  private lateinit var integrityVisitDetailsService: IntegrityVisitDetailsService
  private lateinit var auditService: AuditService
  private lateinit var controller: IntegrityVisitDetailsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    integrityVisitDetailsService = Mockito.mock(IntegrityVisitDetailsService::class.java)
    auditService = Mockito.mock(AuditService::class.java)
    controller = IntegrityVisitDetailsController(integrityVisitDetailsService, auditService)
  }

  @Nested
  inner class GetVisitDetails {
    @Test
    fun `gets order information from order service`() {
      val legacyOrderId = "1ab"
      val expectedResult = listOf(
        IntegrityVisitDetails(
          legacySubjectId = "123",
          address = IntegrityVisitDetails.Address(
            addressLine1 = "3 My street",
            addressLine2 = "My town",
            addressLine3 = "My county",
            addressLine4 = null,
            postcode = "MA77 5AD",
          ),
          actualWorkStartDateTime = LocalDateTime.parse("2020-04-04T00:00:00"),
          actualWorkEndDateTime = LocalDateTime.parse("2020-04-14T00:00:00"),
          visitNotes = "nothing to say",
          visitType = "TEST_VISIT_TYPE",
          visitOutcome = "All good",
        ),
      )

      Mockito.`when`(integrityVisitDetailsService.getVisitDetails(legacyOrderId, false)).thenReturn(expectedResult)

      val result = controller.getVisitDetails(authentication, legacyOrderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(integrityVisitDetailsService, Mockito.times(1)).getVisitDetails(legacyOrderId, false)
    }
  }
}
