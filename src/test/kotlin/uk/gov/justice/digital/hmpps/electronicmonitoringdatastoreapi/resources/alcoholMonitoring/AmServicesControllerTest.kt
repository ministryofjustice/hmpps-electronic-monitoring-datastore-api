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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.alcoholMonitoring.AmServicesController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring.AmServicesService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class AmServicesControllerTest {
  private lateinit var amServicesService: AmServicesService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var amServicesController: AmServicesController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    amServicesService = Mockito.mock(AmServicesService::class.java)
    roleService = Mockito.mock(AthenaRoleService::class.java)
    Mockito.`when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    auditService = Mockito.mock(AuditService::class.java)
    amServicesController = AmServicesController(amServicesService, roleService, auditService)
  }

  @Nested
  inner class GetServices {
    @Test
    fun `gets order information from order service`() {
      val legacyOrderId = "1ab"
      val expectedResult = listOf(
        AmService(
          legacySubjectId = "123",
          legacyOrderId = "321",
          serviceStartDate = LocalDateTime.parse("2001-01-01T00:00:00"),
          serviceEndDate = LocalDateTime.parse("2002-02-02T00:00:00"),
          serviceAddress = "service address",
          equipmentStartDate = LocalDateTime.parse("2003-03-03T00:00:00"),
          equipmentEndDate = LocalDateTime.parse("2004-04-04T00:00:00"),
          hmuSerialNumber = "hmu-01",
          deviceSerialNumber = "device-01",
        ),
      )

      Mockito.`when`(amServicesService.getServices(legacyOrderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = amServicesController.getServices(authentication, legacyOrderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(amServicesService, Mockito.times(1)).getServices(legacyOrderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }
  }
}
