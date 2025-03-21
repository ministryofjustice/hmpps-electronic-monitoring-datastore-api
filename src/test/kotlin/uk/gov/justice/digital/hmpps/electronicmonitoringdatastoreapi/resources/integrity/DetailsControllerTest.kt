package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources.integrity

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.integrity.DetailsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@ActiveProfiles("test")
@JsonTest
class DetailsControllerTest {
  private lateinit var orderService: OrderService
  private lateinit var auditService: AuditService
  private lateinit var controller: DetailsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = mock(Authentication::class.java)
    `when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    orderService = mock(OrderService::class.java)
    auditService = mock(AuditService::class.java)
    controller = DetailsController(orderService, auditService)
  }

  @Nested
  inner class GetOrderDetails {
    @Test
    fun `Returns the appropriate object type`() {
      val legacySubjectId = "1ab"
      val expectedResult = OrderDetails(
        specials = "no",
        legacySubjectId = "",
        legacyOrderId = "",
        offenceRisk = false,
      )

      `when`(orderService.getOrderDetails(legacySubjectId)).thenReturn(expectedResult)

      val result = controller.getDetails(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isNotNull
      Assertions.assertThat(result.body).isInstanceOf(OrderDetails::class.java)
    }

    @Test
    fun `gets order details from order service`() {
      val legacySubjectId = "1ab"
      val expectedResult = OrderDetails(
        specials = "no",
        legacySubjectId = "",
        legacyOrderId = "",
        offenceRisk = false,
      )

      `when`(orderService.getOrderDetails(legacySubjectId)).thenReturn(expectedResult)

      val result = controller.getDetails(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(orderService, times(1)).getOrderDetails(legacySubjectId)
    }
  }
}
