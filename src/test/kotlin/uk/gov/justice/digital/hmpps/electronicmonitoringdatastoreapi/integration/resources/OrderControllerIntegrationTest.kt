package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.resources

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.mocks.MockEmDatastoreClient

@ActiveProfiles("integration")
class OrderControllerIntegrationTest : ControllerIntegrationBase() {
  @Nested
  @DisplayName("GET /orders/getOrderSummary/{orderId}")
  inner class GetOrderSummary {
    @BeforeEach
    fun setup() {
      MockEmDatastoreClient.addResponseFile("successfulKeyOrderInformationResponse")
      MockEmDatastoreClient.addResponseFile("successfulSubjectHistoryReportResponse")
      MockEmDatastoreClient.addResponseFile("successfulDocumentListResponse")
    }

    val baseUri: String = "/orders/getOrderSummary"

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
      val uri = "$baseUri/234"

      webTestClient.get()
        .uri(uri)
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
    }
  }

  @Nested
  @DisplayName("GET /orders/getOrderSummary/specials/{orderId}")
  inner class GetSpecialsOrder {
    @BeforeEach
    fun setup() {
      MockEmDatastoreClient.addResponseFile("successfulKeyOrderInformationResponse")
      MockEmDatastoreClient.addResponseFile("successfulSubjectHistoryReportResponse")
      MockEmDatastoreClient.addResponseFile("successfulDocumentListResponse")
    }

    val baseUri: String = "/orders/getOrderSummary/specials"

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("$baseUri/234")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("$baseUri/234")
    }

    @Test
    fun `should return 403 Forbidden if unknown role is present`() {
      wrongRolesRespondsWithForbiddenTest("$baseUri/234", listOf("ROLE_UNKNOWN"))
    }

    @Test
    fun `should return 403 Forbidden if no specials role is present`() {
      wrongRolesRespondsWithForbiddenTest("$baseUri/234", listOf("ROLE_EM_DATASTORE_GENERAL_RO"))
    }

    // Note: the @Preauthorize on the method is taken in *PREFERENCE* to the preauthorization on the controller
    @Test
    fun `should return 403 Forbidden if no search role is present`() {
      wrongRolesRespondsWithForbiddenTest("$baseUri/234", listOf("ROLE_EM_DATASTORE_RESTRICTED_RO"))
    }

    @Test
    fun `should return 200 OK if all correct roles are present`() {
      val uri = "$baseUri/234"

      webTestClient.get()
        .uri(uri)
        .headers(
          setAuthorisation(
            roles = listOf(
              "ROLE_EM_DATASTORE_RESTRICTED_RO",
              "ROLE_EM_DATASTORE_GENERAL_RO",
            ),
          ),
        )
        .exchange()
        .expectStatus()
        .isOk
    }

    @Test
    fun `should return 200 OK only if both roles are present`() {
      val uri = "$baseUri/234"

      webTestClient.get()
        .uri(uri)
        .headers(
          setAuthorisation(
            roles = listOf(
              "ROLE_EM_DATASTORE_RESTRICTED_RO",
              "ROLE_EM_DATASTORE_GENERAL_RO",
              "ROLE_EM_UNKNOWN_ROLE",
            ),
          ),
        )
        .exchange()
        .expectStatus()
        .isOk
    }
  }

  @Nested
  @DisplayName("GET /orders/{orderId}")
  inner class GetOrder {
    @BeforeEach
    fun setup() {
      MockEmDatastoreClient.addResponseFile("successfulKeyOrderInformationResponse")
      MockEmDatastoreClient.addResponseFile("successfulSubjectHistoryReportResponse")
      MockEmDatastoreClient.addResponseFile("successfulDocumentListResponse")
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/234")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/234")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/234", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should return OK with valid auth header, role`() {
      val uri = "/orders/234"

      webTestClient.get()
        .uri(uri)
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
    }
  }

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
