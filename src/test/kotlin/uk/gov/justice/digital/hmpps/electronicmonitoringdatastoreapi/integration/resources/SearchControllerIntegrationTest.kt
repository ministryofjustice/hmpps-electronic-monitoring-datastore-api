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
  @DisplayName("POST /orders")
  inner class SearchOrders {

    @Test
    fun `should fail with 401 when no authorization header is provided`() {
      webTestClient.post()
        .uri("/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{}")
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should fail with 403 when user has no required roles`() {
      webTestClient.post()
        .uri("/orders")
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
        .uri("/orders")
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

      val requestBody = mapOf(
        "searchType" to "name",
        "legacySubjectId" to "12345",
        "firstName" to "Amy",
        "lastName" to "Smith",
      )

      webTestClient.post()
        .uri("/orders")
        .headers(setAuthorisation())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .exchange()
        .expectStatus()
        .isOk
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.queryExecutionId").isEqualTo("query-execution-id")
    }
  }

  @Nested
  @DisplayName("GET /orders?id={queryExecutionId}")
  inner class GetSearchResults {

    @Test
    fun `should fail with 401 when no authorization header is provided`() {
      webTestClient.get()
        .uri("/orders?id=HT-12345")
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should fail with 403 when user has no required roles`() {
      webTestClient.get()
        .uri("/orders?id=HT-12345")
        .headers(setAuthorisation(roles = listOf()))
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should return 200 with valid configuration`() {
      MockEmDatastoreClient.addResponseFile("successfulOrderSearchResponse")

      webTestClient.get()
        .uri("/orders?id=HT-12345")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.length()").isEqualTo(6)
        .jsonPath("$[0].name").isEqualTo("Amy Smith")
    }

    @Test
    fun `should return 500 with missing query execution id`() {
      MockEmDatastoreClient.addResponseFile("successfulOrderSearchResponse")

      webTestClient.get()
        .uri("/orders")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .is5xxServerError
    }
  }
}
