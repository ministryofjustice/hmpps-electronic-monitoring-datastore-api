package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources.alcoholmonitoring

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AmOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.alcoholmonitoring.AlcoholMonitoringDetailsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AmOrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@ActiveProfiles("test")
@JsonTest
class DetailsControllerTest {
  private lateinit var orderService: OrderService
  private lateinit var amOrderService: AmOrderService
  private lateinit var auditService: AuditService
  private lateinit var controller: AlcoholMonitoringDetailsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = mock(Authentication::class.java)
    `when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    orderService = mock(OrderService::class.java)
    amOrderService = mock(AmOrderService::class.java)
    auditService = mock(AuditService::class.java)
    controller = AlcoholMonitoringDetailsController(orderService, amOrderService, auditService)
  }

  @Nested
  inner class GetAmOrderDetails {
    @Test
    fun `Returns the appropriate object type`() {
      val orderId = "1ab"
      val expectedResult = AmOrderDetails(
        specials = "no",
        legacySubjectId = "",
        legacyOrderId = "",
        responsibleOrgDetailsPhoneNumber = "07777777777",
      )

      `when`(amOrderService.getAmOrderDetails(orderId)).thenReturn(expectedResult)

      val result = controller.getAmOrderDetails(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isNotNull
      Assertions.assertThat(result.body).isInstanceOf(AmOrderDetails::class.java)
    }

    @Test
    fun `gets order details from order service`() {
      val orderId = "1ab"
      val expectedResult = AmOrderDetails(
        specials = "no",
        legacySubjectId = "",
        legacyOrderId = "",
        responsibleOrgDetailsPhoneNumber = "07777777777",
      )

      `when`(amOrderService.getAmOrderDetails(orderId)).thenReturn(expectedResult)

      val result = controller.getAmOrderDetails(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(amOrderService, times(1)).getAmOrderDetails(orderId)
    }
  }
}
