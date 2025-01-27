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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderServicesList
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.OrderServicesController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderServicesService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@ActiveProfiles("test")
@JsonTest
class OrderServicesControllerTest {
  private lateinit var orderServicesService: OrderServicesService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var controller: OrderServicesController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    orderServicesService = Mockito.mock(OrderServicesService::class.java)
    roleService = Mockito.mock(AthenaRoleService::class.java)
    Mockito.`when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.DEV)
    auditService = Mockito.mock(AuditService::class.java)
    controller = OrderServicesController(orderServicesService, roleService, auditService)
  }

  @Nested
  inner class GetServices {
    @Test
    fun `gets order information from order service`() {
      val orderId = "1ab"
      val expectedResult = OrderServicesList(
        pageSize = 1,
        services = listOf(
          OrderService(
            legacySubjectId = 123,
            serviceId = 1,
            serviceAddress1 = "",
            serviceAddress2 = "",
            serviceAddress3 = "",
            serviceAddressPostcode = "TEST+POSTCODE",
            serviceStartDate = "",
            serviceEndDate = "",
            curfewStartDate = "",
            curfewEndDate = "",
            curfewStartTime = "",
            curfewEndTime = "",
            monday = 0,
            tuesday = 0,
            wednesday = 0,
            thursday = 0,
            friday = 1,
            saturday = 1,
            sunday = 0,
          ),
        ),
      )

      Mockito.`when`(orderServicesService.getServices(orderId, AthenaRole.DEV)).thenReturn(expectedResult)

      val result = controller.getServices(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(orderServicesService, Mockito.times(1)).getServices(orderId, AthenaRole.DEV)
    }
  }
}
