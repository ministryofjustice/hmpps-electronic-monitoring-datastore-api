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
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.KeyOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQueryResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderInformationRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.OrderController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@ActiveProfiles("test")
@JsonTest
class OrderControllerTest {
  private lateinit var athenaService: AthenaService
  private lateinit var orderInformationRepository: OrderInformationRepository
  private lateinit var auditService: AuditService
  private lateinit var controller: OrderController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    athenaService = mock()
    orderInformationRepository = mock()
    auditService = mock()
    controller = OrderController(orderInformationRepository, auditService)
    authentication = mock(Authentication::class.java)
    `when`(authentication.principal).thenReturn("MOCK_AUTH_USER")
  }

  @Nested
  inner class GetSpecialsOrder {
    @Test
    fun `is callable with required parameters and correctly uses mocked service`() {
      val orderID = "1ab"
      val expectedResult: OrderInformation = OrderInformationRepository(athenaService).getMockOrderInformation("DIFFERENT ID")

      `when`(orderInformationRepository.getMockOrderInformation(orderID)).thenReturn(expectedResult)

      val result = controller.getSpecialsOrder(authentication, orderID)

      Assertions.assertThat(result.body).isEqualTo(expectedResult)
    }
  }

  @Nested
  inner class GetOrderSummary {

    @Test
    fun `Returns order summary if correct params supplied`() {
      val orderId = "7654321"
      val fakeOrder = OrderInformationRepository(athenaService).getMockOrderInformation("this is fake info")

      val expectedServiceResult = OrderInformationRepository(athenaService)
        .getMockOrderInformation("this is the real info")
        .keyOrderInformation

      val expectedResponse = ResponseEntity.ok(
        OrderInformation(
          keyOrderInformation = expectedServiceResult,
          subjectHistoryReport = fakeOrder.subjectHistoryReport,
          documents = fakeOrder.documents,
        ),
      )

      `when`(authentication.principal).thenReturn("MOCK_AUTH_USER")
      `when`(orderInformationRepository.getMockOrderInformation(orderId))
        .thenReturn(fakeOrder)
      `when`(orderInformationRepository.getKeyOrderInformation(orderId))
        .thenReturn(
          AthenaQueryResponse<KeyOrderInformation>(
            queryString = "fake string",
            athenaRole = "fake role",
            queryResponse = expectedServiceResult,
          ),
        )

      val result: ResponseEntity<OrderInformation> = controller.getOrderSummary(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(expectedResponse.statusCode)
      Assertions.assertThat(result.body).isEqualTo(expectedResponse.body)
      Mockito.verify(orderInformationRepository, times(1)).getMockOrderInformation(orderId)
      Mockito.verify(orderInformationRepository, times(1)).getKeyOrderInformation(orderId)
    }

    // TODO: Replace this with a test that mocks the response
    // TODO: For this to work, need to implement sensible responses from the service layer if order not found
//    @Test
//    fun `Returns 'No summary available' if incorrect orderId`() {
//      val orderId = "non-existent-order"
//      val userToken = "real-token"
//      val expectedResponse = ResponseEntity.notFound().build<Any>()
//
//      val result: ResponseEntity<OrderInformation> = sut.getMockOrderSummary(authentication, orderId)
//      Assertions.assertThat(result.statusCode).isEqualTo(expectedResponse.statusCode)
//      // Checking for body on notFound as it should not contain a body
//      Assertions.assertThat(result.body).isNull()
//    }
  }
}
