package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.EquipmentDetail
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.EquipmentDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.integrity.EquipmentDetailsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.EquipmentDetailsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class EquipmentDetailsControllerTest {
  private lateinit var equipmentDetailsService: EquipmentDetailsService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var controller: EquipmentDetailsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    equipmentDetailsService = Mockito.mock(EquipmentDetailsService::class.java)
    roleService = Mockito.mock(AthenaRoleService::class.java)
    Mockito.`when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    auditService = Mockito.mock(AuditService::class.java)
    controller = EquipmentDetailsController(equipmentDetailsService, roleService, auditService)
  }

  @Nested
  inner class GetEquipmentDetails {
    @Test
    fun `gets order information from order service`() {
      val legacySubjectId = "1ab"
      val expectedResult = listOf<EquipmentDetails>(
        EquipmentDetails(
          legacySubjectId = "123",
          pid = EquipmentDetail(
            id = "123X",
            equipmentCategoryDescription = "TEST_PID_CATEGORY",
            installedDateTime = LocalDateTime.parse("2020-04-04T00:00:00"),
            removedDateTime = LocalDateTime.parse("2020-06-06T00:00:00"),
          ),
          hmu = EquipmentDetail(
            id = "123Y",
            equipmentCategoryDescription = "TEST_HMU_CATEGORY",
            installedDateTime = LocalDateTime.parse("2020-04-04T00:00:00"),
            removedDateTime = LocalDateTime.parse("2020-06-06T00:00:00"),
          ),
        ),
      )

      Mockito.`when`(equipmentDetailsService.getEquipmentDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = controller.getEquipmentDetails(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(equipmentDetailsService, Mockito.times(1)).getEquipmentDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }
  }
}
