package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.resources

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("integration")
class OrderControllerIntegrationTest : ControllerIntegrationBase() {
  @Nested
  @DisplayName("GET /orders/getOrderSummary/{orderId}")
  inner class GetOrderSummary {

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

//     TODO: fix this test - need to mock Athena
//    @Test
//    fun `should return OK with valid auth header, role and X-User-Token`() {
//      val uri = "$baseUri/234"
//
//      webTestClient.get()
//        .uri(uri)
//        .headers(setAuthorisation())
//        .exchange()
//        .expectStatus()
//        .isOk
//    }
  }

  @Nested
  @DisplayName("GET /orders/getOrderSummary/specials/{orderId}")
  inner class GetSpecialsOrder {
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
}
