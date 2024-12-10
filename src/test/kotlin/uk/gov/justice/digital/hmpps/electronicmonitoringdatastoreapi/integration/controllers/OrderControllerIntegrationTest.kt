package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.controllers

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.IntegrationTestBase

class OrderControllerIntegrationTest : IntegrationTestBase() {

  @Nested
  @DisplayName("GET /orders")
  inner class GetOrder {

    val baseUri: String = "/orders"

    @Test
    fun `should return unauthorized if no token`() {
      webTestClient.get()
        .uri(baseUri)
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should return forbidden if no role`() {
      val uri = "$baseUri/234"

      webTestClient.get()
        .uri(uri)
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should return forbidden if wrong role`() {
      val uri = "$baseUri/234"

      webTestClient.get()
        .uri(uri)
        .headers(setAuthorisation(roles = listOf("ROLE_WRONG")))
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should return OK`() {
      val uri = "$baseUri/234"

      webTestClient.get()
        .uri(uri)
        .headers(setAuthorisation(roles = listOf("ELECTRONIC_MONITORING_DATASTORE_API_SEARCH")))
        .exchange()
        .expectStatus()
        .isOk
    }
  }

  @Nested
  @DisplayName("GET /orders/getOrderSummary/{orderId}")
  inner class GetOrderSummary {

    val baseUri: String = "/orders/getOrderSummary"

    @Test
    fun `should return unauthorized if no token`() {
      val uri = "$baseUri/234"

      webTestClient.get()
        .uri(uri)
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should return unauthorized if no role`() {
      val uri = "$baseUri/234"

      webTestClient.get()
        .uri(uri)
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should return unauthorized if wrong role`() {
      val uri = "$baseUri/234"

      webTestClient.get()
        .uri(uri)
        .headers(setAuthorisation(roles = listOf("ROLE_WRONG")))
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    // TODO: fix this test - I do not know how to pass the X-User_Token w/o changing the base test class.
//    @Test
//    fun `should return OK with valid token and role`() {
//      val uri = "$baseUri/234"
//
//      webTestClient.get()
//        .uri(uri)
//        .headers(setAuthorisation(roles = listOf("ELECTRONIC_MONITORING_DATASTORE_API_SEARCH")))
//        .exchange()
//        .expectStatus()
//        .isOk
//    }
  }
}
