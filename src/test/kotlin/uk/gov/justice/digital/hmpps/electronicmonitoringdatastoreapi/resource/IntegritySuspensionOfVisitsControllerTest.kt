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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.IntegritySuspensionOfVisits
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.IntegritySuspensionOfVisitsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class IntegritySuspensionOfVisitsControllerTest {
  private lateinit var integritySuspensionOfVisitsService: IntegritySuspensionOfVisitsService
  private lateinit var auditService: AuditService
  private lateinit var controller: IntegritySuspensionOfVisitsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    integritySuspensionOfVisitsService = Mockito.mock(IntegritySuspensionOfVisitsService::class.java)
    auditService = Mockito.mock(AuditService::class.java)
    controller = IntegritySuspensionOfVisitsController(integritySuspensionOfVisitsService, auditService)
  }

  @Nested
  inner class GetSuspensionOfVisits {
    @Test
    fun `gets order information from order service`() {
      val legacySubjectId = "1ab"
      val expectedResult = listOf(
        IntegritySuspensionOfVisits(
          legacySubjectId = "123",
          suspensionOfVisits = "Yes",
          requestedDate = LocalDateTime.parse("2020-04-04T00:00:00"),
          startDate = LocalDateTime.parse("2020-04-14T00:00:00"),
          startTime = "00:00:00",
          endDate = LocalDateTime.parse("2020-04-24T00:00:00"),
        ),
      )

      Mockito.`when`(integritySuspensionOfVisitsService.getSuspensionOfVisits(legacySubjectId, false)).thenReturn(expectedResult)

      val result = controller.getSuspensionOfVisits(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(integritySuspensionOfVisitsService, Mockito.times(1)).getSuspensionOfVisits(legacySubjectId, false)
    }
  }
}
