package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.controllers

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class OrderControllerIntegrationTest : ControllerIntegrationBase() {

  @Nested
  @DisplayName("GET /orders/getOrderSummary/{orderId}")
  inner class GetOrderSummary {

    val baseUri: String = "/orders/getOrderSummary"

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderTest("$baseUri/234")
    }

    @Test
    fun `should return 401 unauthorized if no role in authorization header`() {
      noRoleInAuthHeaderTest("$baseUri/234")
    }

    @Test
    fun `should return 401 unauthorized if wrong role in authorization header`() {
      wrongRolesTest("$baseUri/234", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should return 403 Forbidden if X-User-Token does not match`() {
      val uri = "$baseUri/234"

      webTestClient.get()
        .uri(uri)
        .headers(setAuthorisation(roles = listOf("ROLE_EM_DATASTORE_GENERAL_RO")))
        .header("X-User-Token", "invalid-token")
        .exchange()
        .expectStatus()
        .isForbidden
    }

//     TODO: fix this test - need to mock Athena
//    @Test
//    fun `should return OK with valid auth header, role and X-User-Token`() {
//      val uri = "$baseUri/234"
//
//      webTestClient.get()
//        .uri(uri)
//        .headers(setAuthorisation(roles = listOf("ROLE_EM_DATASTORE_GENERAL_RO")))
//        .header("X-User-Token", "any-other-string-is-valid")
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
      noAuthHeaderTest("$baseUri/234")
    }

    @Test
    fun `should return 401 unauthorized if no role in authorization header`() {
      noRoleInAuthHeaderTest("$baseUri/234")
    }

    @Test
    fun `should return 403 Forbidden if ONLY specials role is present`() {
      wrongRolesTest("$baseUri/234", listOf("ROLE_EM_DATASTORE_RESTRICTED_RO"))
    }

    // Note: the @Preauthorize on the method is taken in *PREFERENCE* to the preauthorization on the controller
    @Test
    fun `should return 403 Forbidden if ONLY general role is present`() {
      wrongRolesTest("$baseUri/234", listOf("ROLE_EM_DATASTORE_GENERAL_RO"))
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
            ),
          ),
        )
        .header("X-User-Token", "any-other-string-is-valid")
        .exchange()
        .expectStatus()
        .isOk
    }
  }
}
