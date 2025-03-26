package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AmOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.DetailsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AmOrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@ActiveProfiles("test")
@JsonTest
class OrderControllerTest {
  private lateinit var orderService: OrderService
  private lateinit var amOrderService: AmOrderService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var controller: DetailsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = mock(Authentication::class.java)
    `when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    orderService = mock(OrderService::class.java)
    amOrderService = mock(AmOrderService::class.java)
    roleService = mock(AthenaRoleService::class.java)
    `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    auditService = mock(AuditService::class.java)
    controller = DetailsController(orderService, amOrderService, roleService, auditService)
  }

  @Nested
  inner class GetOrderDetails {
    @Test
    fun `Returns the appropriate object type`() {
      val orderId = "1ab"
      val expectedResult: OrderDetails = OrderDetails(
        specials = "no",
        legacySubjectId = "",
        legacyOrderId = "",
        offenceRisk = false,
      )

      `when`(orderService.getOrderDetails(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = controller.getOrderDetails(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isNotNull
      Assertions.assertThat(result.body).isInstanceOf(OrderDetails::class.java)
    }

    @Test
    fun `gets order details from order service`() {
      val orderId = "1ab"
      val expectedResult: OrderDetails = OrderDetails(
        specials = "no",
        legacySubjectId = "",
        legacyOrderId = "",
        offenceRisk = false,
      )

      `when`(orderService.getOrderDetails(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = controller.getOrderDetails(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(orderService, times(1)).getOrderDetails(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }
  }

  @Nested
  inner class GetAmOrderDetails {
    @Test
    fun `Returns the appropriate object type`() {
      val orderId = "1ab"
      val expectedResult: AmOrderDetails = AmOrderDetails(
        specials = "no",
        legacySubjectId = "",
        legacyOrderId = "",
        responsibleOrgDetailsPhoneNumber = "07777777777",
      )

      `when`(amOrderService.getAmOrderDetails(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = controller.getAmOrderDetails(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isNotNull
      Assertions.assertThat(result.body).isInstanceOf(AmOrderDetails::class.java)
    }

    @Test
    fun `gets order details from order service`() {
      val orderId = "1ab"
      val expectedResult: AmOrderDetails = AmOrderDetails(
        specials = "no",
        legacySubjectId = "",
        legacyOrderId = "",
        responsibleOrgDetailsPhoneNumber = "07777777777",
      )

      `when`(amOrderService.getAmOrderDetails(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = controller.getAmOrderDetails(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(amOrderService, times(1)).getAmOrderDetails(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }
  }
}
