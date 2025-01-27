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
  @DisplayName("GET /orders/{orderId}/monitoring-events")
  inner class GetMonitoringEvents {
    @BeforeEach
    fun setup() {
      MockEmDatastoreClient.addResponseFile("successfulMonitoringEventListResponse")
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/234/monitoring-events")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/234/monitoring-events")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/234/monitoring-events", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should return OK with valid auth header, role`() {
      val uri = "/orders/234/monitoring-events"

      webTestClient.get()
        .uri(uri)
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
    }
  }

  @Nested
  @DisplayName("GET /orders/{orderId}/incident-events")
  inner class GetIncidentEvents {
    @BeforeEach
    fun setup() {
      MockEmDatastoreClient.addResponseFile("successfulIncidentEventListResponse")
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/234/incident-events")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/234/incident-events")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/234/incident-events", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should return OK with valid auth header, role`() {
      val uri = "/orders/234/incident-events"

      webTestClient.get()
        .uri(uri)
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
    }
  }

  @Nested
  @DisplayName("GET /orders/{orderId}/contact-events")
  inner class GetContactEvents {
    @BeforeEach
    fun setup() {
      MockEmDatastoreClient.addResponseFile("successfulContactEventListResponse")
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/234/contact-events")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/234/contact-events")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/234/contact-events", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should return OK with valid auth header, role`() {
      val uri = "/orders/234/contact-events"

      webTestClient.get()
        .uri(uri)
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
    }
  }
}
