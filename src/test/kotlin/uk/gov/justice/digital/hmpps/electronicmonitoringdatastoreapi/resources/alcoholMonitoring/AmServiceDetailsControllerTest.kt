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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AmServiceDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring.AmServiceDetailsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class AmServiceDetailsControllerTest {
  private lateinit var amServiceDetailsService: AmServiceDetailsService
  private lateinit var auditService: AuditService
  private lateinit var amServiceDetailsController: AmServiceDetailsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    amServiceDetailsService = Mockito.mock(AmServiceDetailsService::class.java)
    auditService = Mockito.mock(AuditService::class.java)
    amServiceDetailsController = AmServiceDetailsController(amServiceDetailsService, auditService)
  }

  @Nested
  inner class GetServiceDetails {
    @Test
    fun `gets service details from service details service`() {
      val legacyOrderId = "1ab"
      val expectedResult = listOf(
        AmServiceDetails(
          legacySubjectId = "123",
          serviceStartDate = LocalDateTime.parse("2001-01-01T00:00:00"),
          serviceEndDate = LocalDateTime.parse("2002-02-02T00:00:00"),
          serviceAddress = "service address",
          equipmentStartDate = LocalDateTime.parse("2003-03-03T00:00:00"),
          equipmentEndDate = LocalDateTime.parse("2004-04-04T00:00:00"),
          hmuSerialNumber = "hmu-01",
          deviceSerialNumber = "device-01",
        ),
      )

      Mockito.`when`(amServiceDetailsService.getServiceDetails(legacyOrderId)).thenReturn(expectedResult)

      val result = amServiceDetailsController.getServiceDetails(authentication, legacyOrderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(amServiceDetailsService, Mockito.times(1)).getServiceDetails(legacyOrderId)
    }
  }
}
