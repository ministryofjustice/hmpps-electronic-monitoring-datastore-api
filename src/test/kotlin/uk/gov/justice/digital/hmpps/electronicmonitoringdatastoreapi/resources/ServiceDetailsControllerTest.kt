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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.ServiceDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.integrity.ServiceDetailsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.ServiceDetailsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class ServiceDetailsControllerTest {
  private lateinit var serviceDetailsService: ServiceDetailsService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var controller: ServiceDetailsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    serviceDetailsService = Mockito.mock(ServiceDetailsService::class.java)
    roleService = Mockito.mock(AthenaRoleService::class.java)
    Mockito.`when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    auditService = Mockito.mock(AuditService::class.java)
    controller = ServiceDetailsController(serviceDetailsService, roleService, auditService)
  }

  @Nested
  inner class GetServiceDetails {
    @Test
    fun `gets order information from order service`() {
      val legacySubjectId = "1ab"
      val expectedResult = listOf(
        ServiceDetails(
          legacySubjectId = "123",
          serviceId = 1,
          serviceAddress1 = "",
          serviceAddress2 = "",
          serviceAddress3 = "",
          serviceAddressPostcode = "TEST+POSTCODE",
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

      Mockito.`when`(serviceDetailsService.getServiceDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = controller.getServiceDetails(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(serviceDetailsService, Mockito.times(1)).getServiceDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }
  }
}
