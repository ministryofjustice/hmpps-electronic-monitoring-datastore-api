package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import net.minidev.json.JSONObject
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderInformationRepository

class OrderControllerTest {

  private val objectMapper = ObjectMapper()

  @Nested
  inner class GetOrder {

    private val sut: OrderController = OrderController()

    @Test
    fun `Returns data if correct params supplied`() {
      val orderId = "obviously-real-id"
      val userToken = "real-token"
      val expected = JSONObject(
        mapOf("data" to "This is the data for order $orderId"),
      )

      val result: JSONObject = sut.getOrder(orderId, userToken)
      Assertions.assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Returns 'Order not found' if incorrect orderId`() {
      val orderId = "invalid-order"
      val userToken = "real-token"
      val expected = JSONObject(
        mapOf("data" to "No order with ID $orderId could be found"),
      )

      val result: JSONObject = sut.getOrder(orderId, userToken)
      Assertions.assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `returns "Unauthorised request" if userToken is invalid`() {
      val orderId = "obviously-real-id"
      val userToken = "nonsense-token"
      val expected = JSONObject(
        mapOf("data" to "Unauthorised request with user token $userToken"),
      )

      val result: JSONObject = sut.getOrder(orderId, userToken)
      Assertions.assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `returns "Unauthorised request" if userToken is not supplied`() {
      val orderId = "obviously-real-id"
      val userToken = "no-token-supplied" // Default value used explicitly
      val expected = JSONObject(
        mapOf("data" to "Unauthorised request with user token $userToken"),
      )

      val result: JSONObject = sut.getOrder(orderId, userToken)
      Assertions.assertThat(result).isEqualTo(expected)
    }
  }

  @Nested
  inner class GetOrderSummary {

    private val sut: OrderController = OrderController()
    private val repository: OrderInformationRepository = OrderInformationRepository()

    // TODO: Replace this with a test that mocks the response
    @Test
    fun `Returns order summary if correct params supplied`() {
      val orderId = "7654321"
      val userToken = "real-token"
      val orderInfo = repository.getMockOrderInformation(orderId)
      val expectedResponse = ResponseEntity.ok(orderInfo)

      val result: ResponseEntity<OrderInformation> = sut.getMockOrderSummary(orderId, userToken)
      Assertions.assertThat(result.statusCode).isEqualTo(expectedResponse.statusCode)
      Assertions.assertThat(result.body).isEqualTo(expectedResponse.body)
    }

    // TODO: Replace this with a test that mocks the response
//    @Test
//    fun `Returns 'No summary available' if incorrect orderId`() {
//      val orderId = "non-existent-order"
//      val userToken = "real-token"
//      val expectedResponse = ResponseEntity.notFound().build<Any>()
//
//      val result: ResponseEntity<OrderInformation> = sut.getMockOrderSummary(orderId, userToken)
//      Assertions.assertThat(result.statusCode).isEqualTo(expectedResponse.statusCode)
//      // Checking for body on notFound as it should not contain a body
//      Assertions.assertThat(result.body).isNull()
//    }

    @Test
    fun `Throws AccessDeniedException if userToken is invalid`() {
      val orderId = "obviously-real-id"
      val userToken = "fake-token"

      Assertions.assertThatExceptionOfType(AccessDeniedException::class.java)
        .isThrownBy { sut.getOrderSummary(orderId, userToken) }
        .withMessage("Your token is valid for this service, but your user is not allowed to access this resource")
    }
  }

  @Nested
  inner class CheckValidUser {

    private val sut: OrderController = OrderController()

    @Test
    fun `Returns true if token is valid`() {
      val userToken = "real-token"
      val result = sut.checkValidUser(userToken)
      Assertions.assertThat(result).isTrue
    }

    @Test
    fun `Returns false if token is invalid`() {
      val userToken = "invalid-token"
      val result = sut.checkValidUser(userToken)
      Assertions.assertThat(result).isFalse
    }
  }
}
