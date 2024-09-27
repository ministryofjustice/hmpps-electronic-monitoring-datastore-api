package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.controllers

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.IntegrationTestBase

class SearchControllerIntegrationTest : IntegrationTestBase() {

  @Nested
  @DisplayName("GET /search/cases")
  inner class GetCases {

    val baseUri: String = "/example/time"

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
      webTestClient.get()
        .uri(baseUri)
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should return forbidden if wrong role`() {
      webTestClient.get()
        .uri(baseUri)
        .headers(setAuthorisation(roles = listOf("ROLE_WRONG")))
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should return OK`() {
      webTestClient.get()
        .uri(baseUri)
        .headers(setAuthorisation(roles = listOf("ELECTRONIC_MONITORING_DATASTORE_API_SEARCH")))
        .exchange()
        .expectStatus()
        .isOk
    }
  }
}
