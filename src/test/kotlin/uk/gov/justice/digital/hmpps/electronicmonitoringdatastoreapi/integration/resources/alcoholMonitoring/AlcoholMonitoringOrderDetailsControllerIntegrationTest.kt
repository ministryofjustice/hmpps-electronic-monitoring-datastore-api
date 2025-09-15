package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.resources.alcoholMonitoring

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_RESTRICTED__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.resources.ControllerIntegrationBase
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockEmDatastoreClient

@ActiveProfiles("integration")
class AlcoholMonitoringOrderDetailsControllerIntegrationTest : ControllerIntegrationBase() {

  @Nested
  @DisplayName("POST /orders/alcohol-monitoring")
  inner class SearchOrders {

    @Test
    fun `should fail with 401 when no authorization header is provided`() {
      val requestBody = mapOf(
        "legacySubjectId" to "12345",
      )

      webTestClient.post()
        .uri("/orders/alcohol-monitoring")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should fail with 403 when user has no required roles`() {
      val requestBody = mapOf(
        "legacySubjectId" to "12345",
      )

      webTestClient.post()
        .uri("/orders/alcohol-monitoring")
        .headers(setAuthorisation(roles = listOf()))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should fail with 400 BAD REQUEST if empty body`() {
      webTestClient.post()
        .uri("/orders/alcohol-monitoring")
        .headers(setAuthorisation())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{}")
        .exchange()
        .expectStatus()
        .is4xxClientError
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.userMessage").isEqualTo("Validation failure: This request is malformed, there must be at least one search criteria present")
    }

    @Test
    fun `should return 200 with valid body and alcohol-monitoring search type`() {
      MockEmDatastoreClient.addResponseFile("successfulGetQueryExecutionIdResponse")

      val requestBody = mapOf(
        "legacySubjectId" to "12345",
        "firstName" to "Pheobe",
        "lastName" to "Smith",
      )

      webTestClient.post()
        .uri("/orders/alcohol-monitoring")
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
  @DisplayName("GET /orders/alcohol-monitoring?id={queryExecutionId}")
  inner class GetAlcoholMonitoringSearchResults {

    @Test
    fun `should fail with 401 when no authorization header is provided`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring?id=HT-12345")
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should fail with 403 when user has no required roles`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring?id=HT-12345")
        .headers(setAuthorisation(roles = listOf()))
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should return 200 with valid configuration`() {
      MockEmDatastoreClient.addResponseFile("successfulOrderSearchResponse")

      webTestClient.get()
        .uri("/orders/alcohol-monitoring?id=HT-12345")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.length()").isEqualTo(6)
        .jsonPath("$[0].firstName").isEqualTo("Pheobe")
        .jsonPath("$[0].lastName").isEqualTo("Smith")
    }

    @Test
    fun `should return 500 with missing query execution id`() {
      MockEmDatastoreClient.addResponseFile("successfulOrderSearchResponse")

      webTestClient.get()
        .uri("/orders/alcohol-monitoring")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .is5xxServerError
    }
  }

  @Nested
  @DisplayName("GET /orders/alcohol-monitoring/{legacySubjectId}")
  inner class GetDetails {
    @BeforeEach
    fun setup() {
      MockEmDatastoreClient.addResponseFile("successfulAlcoholMonitoringOrderDetailsResponse")
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/alcohol-monitoring/234")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/alcohol-monitoring/234")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/alcohol-monitoring/234", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/2_4")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/234")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }

    @Test
    fun `should return OK with valid auth header and role for accessing general orders`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/234")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
    }
  }
}
