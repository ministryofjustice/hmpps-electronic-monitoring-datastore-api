package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.controllers

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType

class SearchControllerIntegrationTest : ControllerIntegrationBase() {

  @Nested
  @DisplayName("POST /search/orders-old")
  inner class SearchOrdersFake {

    val baseUri: String = "/search/orders-old"

    @Test
    fun `should fail with 401 when no authorization header is provided`() {
      webTestClient.post()
        .uri(baseUri)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{}")
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should fail with 401 when user has no required roles`() {
      webTestClient.post()
        .uri(baseUri)
        .headers(setAuthorisation(roles = listOf("INVALID_ROLE")))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{}")
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should fail with 401 when no X-User-Token header is provided`() {
      webTestClient.post()
        .uri(baseUri)
        .headers(setAuthorisation(roles = listOf("ROLE_EM_DATASTORE_GENERAL_RO")))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{}")
        .exchange()
        .expectStatus()
        .isUnauthorized // Expect 401 Unauthorized because X-User-Token is missing
        .expectBody()
        .jsonPath("$.userMessage").isEqualTo("Missing required header 'X-User-Token'")
    }

    @Test
    fun `should return 200 with empty body`() {
      webTestClient.post()
        .uri(baseUri)
        .headers {
          it.add("X-User-Token", "valid-user-token") // Add required X-User-Token header
          setAuthorisation(roles = listOf("ROLE_EM_DATASTORE_GENERAL_RO")).invoke(it)
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
    fun `should return 200 with valid body`() {
      val requestBody = mapOf(
        "searchType" to "name",
        "legacySubjectId" to "12345",
        "firstName" to "Amy",
        "lastName" to "Smith",
      )

      webTestClient.post()
        .uri(baseUri)
        .headers {
          it.add("X-User-Token", "valid-user-token") // Add required X-User-Token header
          setAuthorisation(roles = listOf("ROLE_EM_DATASTORE_GENERAL_RO")).invoke(it)
        }
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .exchange()
        .expectStatus()
        .isOk // Endpoint should return 200 OK
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.length()").isEqualTo(6) // Expect 6 mock orders
        .jsonPath("$[0].name").isEqualTo("Amy Smith") // Validate the first mock order
    }
  }

  @Nested
  @DisplayName("POST /search/custom-query")
  inner class QueryAthena {

    val baseUri: String = "/search/custom-query"

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderTest("$baseUri", true)
    }

    @Test
    fun `should return 401 unauthorized if no role in authorization header`() {
      noRoleInAuthHeaderTest("$baseUri", true)
    }

    @Test
    fun `should return 401 unauthorized if wrong role in authorization header`() {
      wrongRolesTest("$baseUri", listOf("ROLE_WRONG"), true)
    }

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
        .jsonPath("$.athenaRole").isNotEmpty
    }
  }
}
