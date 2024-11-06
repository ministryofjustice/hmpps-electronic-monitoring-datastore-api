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
}
