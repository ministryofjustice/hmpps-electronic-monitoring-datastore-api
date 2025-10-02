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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.IntegrityEquipmentDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.IntegrityEquipmentDetailsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class IntegrityEquipmentDetailsControllerTest {
  private lateinit var integrityEquipmentDetailsService: IntegrityEquipmentDetailsService
  private lateinit var auditService: AuditService
  private lateinit var controller: IntegrityEquipmentDetailsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    integrityEquipmentDetailsService = Mockito.mock(IntegrityEquipmentDetailsService::class.java)
    auditService = Mockito.mock(AuditService::class.java)
    controller = IntegrityEquipmentDetailsController(integrityEquipmentDetailsService, auditService)
  }

  @Nested
  inner class GetEquipmentDetails {
    @Test
    fun `gets order information from order service`() {
      val legacySubjectId = "1ab"
      val expectedResult = listOf(
        IntegrityEquipmentDetails(
          legacySubjectId = "123",
          pid = IntegrityEquipmentDetails.Equipment(
            id = "123X",
            equipmentCategoryDescription = "TEST_PID_CATEGORY",
            installedDateTime = LocalDateTime.parse("2020-04-04T00:00:00"),
            removedDateTime = LocalDateTime.parse("2020-06-06T00:00:00"),
          ),
          hmu = IntegrityEquipmentDetails.Equipment(
            id = "123Y",
            equipmentCategoryDescription = "TEST_HMU_CATEGORY",
            installedDateTime = LocalDateTime.parse("2020-04-04T00:00:00"),
            removedDateTime = LocalDateTime.parse("2020-06-06T00:00:00"),
          ),
        ),
      )

      Mockito.`when`(integrityEquipmentDetailsService.getEquipmentDetails(legacySubjectId, false)).thenReturn(expectedResult)

      val result = controller.getEquipmentDetails(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(integrityEquipmentDetailsService, Mockito.times(1)).getEquipmentDetails(legacySubjectId, false)
    }
  }
}
