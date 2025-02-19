package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.resources

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.mocks.MockEmDatastoreClient

@ActiveProfiles("integration")
class SearchControllerIntegrationTest : ControllerIntegrationBase() {
  @Nested
  @DisplayName("POST /search/orders")
  inner class SearchOrders {

    private val baseUri: String = "/search/orders"

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
    fun `should fail with 403 when user has no required roles`() {
      webTestClient.post()
        .uri(baseUri)
        .headers(setAuthorisation(roles = listOf()))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{}")
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should fail with 400 BAD REQUEST if empty body`() {
      webTestClient.post()
        .uri(baseUri)
        .headers(setAuthorisation())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{}")
        .exchange()
        .expectStatus()
        .isBadRequest
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.userMessage").isEqualTo("This request is malformed, and may be missing a body")
    }

    @Test
    fun `should return 200 with valid body`() {
      MockEmDatastoreClient.addResponseFile("successfulGetQueryExecutionIdResponse")
//      MockEmDatastoreClient.addResponseFile("successfulOrderSearchResponse")

      val requestBody = mapOf(
        "searchType" to "name",
        "legacySubjectId" to "12345",
        "firstName" to "Amy",
        "lastName" to "Smith",
      )

      webTestClient.post()
        .uri(baseUri)
        .headers(setAuthorisation())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .exchange()
        .expectStatus()
        .isOk
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.queryExecutionId").isEqualTo("query-execution-id") // Validate the first mock order
    }
  }

  @Nested
  @DisplayName("GET /search/results")
  inner class GetSearchResults {

    private val baseUri: String = "/search/results/query-execution-id"

    @Test
    fun `should fail with 401 when no authorization header is provided`() {
      webTestClient.get()
        .uri(baseUri)
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should fail with 403 when user has no required roles`() {
      webTestClient.get()
        .uri(baseUri)
        .headers(setAuthorisation(roles = listOf()))
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should return 200 with valid configuration`() {
      MockEmDatastoreClient.addResponseFile("successfulOrderSearchResponse")

      webTestClient.get()
        .uri(baseUri)
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
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
    private val baseUri: String = "/search/custom-query"

    private val requestBody = mapOf(
      "queryString" to "fake-test-querystring",
    )
    private val failingRequestBody = mapOf(
      "queryString" to "THROW ERROR",
    )

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      webTestClient.post()
        .uri(baseUri)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      webTestClient.post()
        .uri(baseUri)
        .headers(setAuthorisation(roles = listOf()))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      webTestClient.post()
        .uri(baseUri)
        .headers(setAuthorisation(roles = listOf("ROLE_PROBATION")))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should fail when no body is sent`() {
      webTestClient.post()
        .uri(baseUri)
        .headers(setAuthorisation())
        .contentType(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should fail with empty body with 400 error`() {
      webTestClient.post()
        .uri(baseUri)
        .headers(setAuthorisation())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{}")
        .exchange()
        .expectStatus()
        .isBadRequest
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.userMessage").isEqualTo("This request is malformed, and may be missing a body")
    }

    @Test
    fun `should return a 200 ok AthenaQueryResponse which is errored with valid body`() {
      webTestClient.post()
        .uri(baseUri)
        .headers(setAuthorisation())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(failingRequestBody) // Sending a valid body
        .exchange()
        .expectStatus()
        .isOk // Endpoint should handle a valid body
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.queryString").isEqualTo("THROW ERROR")
        .jsonPath("$.isErrored").isEqualTo("true")
        .jsonPath("$.athenaRole").isNotEmpty
    }
  }
}
