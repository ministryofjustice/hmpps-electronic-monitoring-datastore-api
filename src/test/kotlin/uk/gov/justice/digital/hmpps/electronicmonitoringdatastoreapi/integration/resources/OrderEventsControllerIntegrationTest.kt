package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.resources

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.mocks.MockEmDatastoreClient

@ActiveProfiles("integration")
class OrderEventsControllerIntegrationTest : ControllerIntegrationBase() {

  @Nested
  @DisplayName("GET /orders/getIncidentEvents/{orderId}")
  inner class GetIncidentEvents {
    val baseUri = "/orders/getIncidentEvents"

    @BeforeEach
    fun setup() {
      MockEmDatastoreClient.addResponseFile("successfulIncidentEventsResponse")
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("$baseUri/234")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("$baseUri/234")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("$baseUri/234", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should return OK with valid auth header, role`() {
      webTestClient.get()
        .uri("$baseUri/234")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
    }
  }

  @Nested
  @DisplayName("GET /orders/getViolationEvents/{orderId}")
  inner class GetViolationEvents {
    val baseUri = "/orders/getViolationEvents"

    @BeforeEach
    fun setup() {
      MockEmDatastoreClient.addResponseFile("successfulViolationEventsResponse")
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("$baseUri/234")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("$baseUri/234")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("$baseUri/234", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should return OK with valid auth header, role`() {
      webTestClient.get()
        .uri("$baseUri/234")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
    }
  }

  @Nested
  @DisplayName("GET /orders/getContactEvents/{orderId}")
  inner class GetContactEvents {
    val baseUri = "/orders/getContactEvents"

    @BeforeEach
    fun setup() {
      MockEmDatastoreClient.addResponseFile("successfulContactEventsResponse")
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("$baseUri/234")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("$baseUri/234")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("$baseUri/234", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should return OK with valid auth header, role`() {
      webTestClient.get()
        .uri("$baseUri/234")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
    }
  }
}
