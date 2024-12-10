package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import net.minidev.json.JSONObject
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class OrderControllerTest {

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
