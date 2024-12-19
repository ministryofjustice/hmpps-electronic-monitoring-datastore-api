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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AthenaQueryResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.KeyOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderInformationRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.OrderController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@ActiveProfiles("test")
@JsonTest
class OrderControllerTest {
  private val repository: OrderInformationRepository = mock()
  private val auditService: AuditService = mock()
  private val controller = OrderController(repository, auditService)
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = mock(Authentication::class.java)
    `when`(authentication.principal).thenReturn("MOCK_AUTH_USER")
  }

  // @Nested
  // inner class GetOrder {

  //   @Test
  //   fun `Returns data if correct params supplied`() {
  //     val orderId = "obviously-real-id"
  //     val userToken = "real-token"
  //     val expected = JSONObject(
  //       mapOf("data" to "This is the data for order obviously-real-id"),
  //     )

  //     val result: JSONObject = sut.getOrder(orderId, userToken)
  //     Assertions.assertThat(result).isEqualTo(expected)
  //   }

  //   @Test
  //   fun `Returns 'Order not found' if incorrect orderId`() {
  //     val orderId = "invalid-order"
  //     val userToken = "real-token"
  //     val expected = JSONObject(
  //       mapOf("data" to "No order with ID invalid-order could be found"),
  //     )

  //     val result: JSONObject = sut.getOrder(orderId, userToken)
  //     Assertions.assertThat(result).isEqualTo(expected)
  //   }

  //   @Test
  //   fun `returns 'Unauthorised request' if userToken is invalid`() {
  //     val orderId = "obviously-real-id"
  //     val userToken = "nonsense-token"
  //     val expected = JSONObject(
  //       mapOf("data" to "Unauthorised request with user token nonsense-token"),
  //     )

  //     val result: JSONObject = sut.getOrder(orderId, userToken)
  //     Assertions.assertThat(result).isEqualTo(expected)
  //   }

  //   @Test
  //   fun `returns 'Unauthorised request' if userToken is not supplied`() {
  //     val orderId = "obviously-real-id"
  //     val expected = JSONObject(
  //       mapOf("data" to "Unauthorised request with user token no-token-supplied"),
  //     )

  //     val result: JSONObject = sut.getOrder(orderId)
  //     Assertions.assertThat(result).isEqualTo(expected)
  //   }
  // }

  @Nested
  inner class GetOrderSummary {


    @Test
    fun `Returns order summary if correct params supplied`() {
      val orderId = "7654321"
      val fakeOrder = OrderInformationRepository().getMockOrderInformation("this is fake info")

      val expectedServiceResult = OrderInformationRepository()
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
      `when`(repository.getMockOrderInformation(orderId))
        .thenReturn(fakeOrder)
      `when`(repository.getKeyOrderInformation(orderId))
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
      Mockito.verify(repository, times(1)).getMockOrderInformation(orderId)
      Mockito.verify(repository, times(1)).getKeyOrderInformation(orderId)
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
