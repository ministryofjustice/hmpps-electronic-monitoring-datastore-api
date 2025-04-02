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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmEquipmentDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.alcoholMonitoring.AmEquipmentDetailsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring.AmEquipmentDetailsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class AmEquipmentDetailsControllerTest {
  private lateinit var amEquipmentDetailsService: AmEquipmentDetailsService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var amEquipmentDetailsController: AmEquipmentDetailsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    amEquipmentDetailsService = Mockito.mock(AmEquipmentDetailsService::class.java)
    roleService = Mockito.mock(AthenaRoleService::class.java)
    Mockito.`when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    auditService = Mockito.mock(AuditService::class.java)
    amEquipmentDetailsController = AmEquipmentDetailsController(amEquipmentDetailsService, roleService, auditService)
  }

  @Nested
  inner class GetEquipmentDetails {
    @Test
    fun `gets equipment details from equipment details service`() {
      val legacyOrderId = "321"
      val expectedResult = listOf(
        AmEquipmentDetails(
          legacySubjectId = "123",
          legacyOrderId = "321",
          deviceType = "tag",
          deviceSerialNumber = "740",
          deviceAddressType = "secondary",
          legFitting = "right",
          deviceInstalledDateTime = LocalDateTime.parse("2001-01-01T01:10:10"),
          deviceRemovedDateTime = LocalDateTime.parse("2002-02-02T02:20:20"),
          hmuInstallDateTime = null,
          hmuUninstallDateTime = null,
        ),
      )

      Mockito.`when`(amEquipmentDetailsService.getEquipmentDetails(legacyOrderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = amEquipmentDetailsController.getEquipmentDetails(authentication, legacyOrderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(amEquipmentDetailsService, Mockito.times(1)).getEquipmentDetails(legacyOrderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }
  }
}
