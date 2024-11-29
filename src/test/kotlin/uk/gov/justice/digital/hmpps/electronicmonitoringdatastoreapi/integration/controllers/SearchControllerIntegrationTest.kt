package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.controllers

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.IntegrationTestBase

class SearchControllerIntegrationTest : IntegrationTestBase() {

  @Nested
  @DisplayName("GET /search/cases")
  inner class GetCases {

    val baseUri: String = "/search/cases"

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
  @DisplayName("POST /search/orders")
  inner class SearchOrders {

    val baseUri: String = "/search/orders"

    @Test
    fun `should fail when no body is sent`() {
      webTestClient.post()
        .uri(baseUri)
        .headers(setAuthorisation(roles = listOf("ELECTRONIC_MONITORING_DATASTORE_API_SEARCH")))
        .contentType(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isUnauthorized // Expect a 400 Bad Request when no body is sent
    }

    @Test
    fun `should fail when no user token is provided`() {
      webTestClient.post()
        .uri(baseUri)
        .headers(setAuthorisation(roles = listOf("ELECTRONIC_MONITORING_DATASTORE_API_SEARCH"))) // No X-User-Token header
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{}")
        .exchange()
        .expectStatus()
        .isUnauthorized // Expect 401 Unauthorized because X-User-Token is missing
        .expectBody()
        .jsonPath("$.userMessage").isEqualTo("Missing required header 'X-User-Token'")
    }

    @Test
    fun `should succeed with empty body`() {
      webTestClient.post()
        .uri(baseUri)
        .headers {
          it.add("X-User-Token", "valid-user-token") // Add the required X-User-Token header
          setAuthorisation(roles = listOf("ELECTRONIC_MONITORING_DATASTORE_API_SEARCH")).invoke(it)
        }
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{}")
        .exchange()
        .expectStatus()
        .isOk // Endpoint should handle an empty body
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.length()").isEqualTo(6) // Expect 6 mock orders returned
    }

    @Test
    fun `should succeed with valid body`() {
      val requestBody = mapOf(
        "searchType" to "name",
        "legacySubjectId" to "12345",
        "firstName" to "Amy",
        "lastName" to "Smith",
      )

      webTestClient.post()
        .uri(baseUri)
        .headers {
          it.add("X-User-Token", "valid-user-token") // Add the required X-User-Token header
          setAuthorisation(roles = listOf("ELECTRONIC_MONITORING_DATASTORE_API_SEARCH")).invoke(it)
        }
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody) // Sending a valid body
        .exchange()
        .expectStatus()
        .isOk // Endpoint should handle a valid body
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.length()").isEqualTo(6) // Expect 6 mock orders returned
        .jsonPath("$[0].name").isEqualTo("Amy Smith") // Validate specific fields of the mock data
        .jsonPath("$[0].dataType").isEqualTo("am")
    }
  }

  @Nested
  @DisplayName("POST /search/custom-query")
  inner class QueryAthena {

    val baseUri: String = "/search/custom-query"

    @Test
    fun `should fail when no body is sent`() {
      webTestClient.post()
        .uri(baseUri)
        .headers(setAuthorisation(roles = listOf("ELECTRONIC_MONITORING_DATASTORE_API_SEARCH")))
        .contentType(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isUnauthorized // Expect a 400 Bad Request when no body is sent
    }

    @Test
    fun `should fail when no user token is provided`() {
      webTestClient.post()
        .uri(baseUri)
        .headers(setAuthorisation(roles = listOf("ELECTRONIC_MONITORING_DATASTORE_API_SEARCH"))) // No X-User-Token header
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{}")
        .exchange()
        .expectStatus()
        .isUnauthorized // Expect 401 Unauthorized because X-User-Token is missing
        .expectBody()
        .jsonPath("$.userMessage").isEqualTo("Missing required header 'X-User-Token'")
    }

    @Test
    fun `should fail with empty body with 500 error`() {
      webTestClient.post()
        .uri(baseUri)
        .headers {
          it.add("X-User-Token", "valid-user-token") // Add the required X-User-Token header
          setAuthorisation(roles = listOf("ELECTRONIC_MONITORING_DATASTORE_API_SEARCH")).invoke(it)
        }
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{}")
        .exchange()
        .expectStatus()
        .is5xxServerError
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
    }

    @Test
    fun `should return correct fields with valid body`() {
      val requestBody = mapOf(
        "queryString" to "fake-test-querystring",
      )

      webTestClient.post()
        .uri(baseUri)
        .headers {
          it.add("X-User-Token", "valid-user-token") // Add the required X-User-Token header
          setAuthorisation(roles = listOf("ELECTRONIC_MONITORING_DATASTORE_API_SEARCH")).invoke(it)
        }
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody) // Sending a valid body
        .exchange()
        .expectStatus()
        .isOk // Endpoint should handle a valid body
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.queryString").isEqualTo("fake-test-querystring")
        .jsonPath("$.isErrored").isNotEmpty
    }
  }
}
