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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.IntegrityServiceDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.IntegrityServiceDetailsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class IntegrityServiceDetailsControllerTest {
  private lateinit var integrityServiceDetailsService: IntegrityServiceDetailsService
  private lateinit var auditService: AuditService
  private lateinit var controller: IntegrityServiceDetailsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    integrityServiceDetailsService = Mockito.mock(IntegrityServiceDetailsService::class.java)
    auditService = Mockito.mock(AuditService::class.java)
    controller = IntegrityServiceDetailsController(integrityServiceDetailsService, auditService)
  }

  @Nested
  inner class GetServiceDetails {
    @Test
    fun `gets order information from order service`() {
      val legacySubjectId = "1ab"
      val expectedResult = listOf(
        IntegrityServiceDetails(
          legacySubjectId = "123",
          serviceId = 1,
          serviceAddress1 = "",
          serviceAddress2 = "",
          serviceAddress3 = "",
          serviceAddressPostCode = "TEST+POSTCODE",
          serviceStartDate = LocalDateTime.parse("2020-04-04T00:00:00"),
          serviceEndDate = LocalDateTime.parse("2020-04-14T00:00:00"),
          curfewStartDate = LocalDateTime.parse("2020-04-06T00:00:00"),
          curfewEndDate = LocalDateTime.parse("2020-04-08T00:00:00"),
          monday = 0,
          tuesday = 0,
          wednesday = 0,
          thursday = 0,
          friday = 1,
          saturday = 1,
          sunday = 0,
        ),
      )

      Mockito.`when`(integrityServiceDetailsService.getServiceDetails(legacySubjectId, false)).thenReturn(expectedResult)

      val result = controller.getServiceDetails(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(integrityServiceDetailsService, Mockito.times(1)).getServiceDetails(legacySubjectId, false)
    }
  }
}
