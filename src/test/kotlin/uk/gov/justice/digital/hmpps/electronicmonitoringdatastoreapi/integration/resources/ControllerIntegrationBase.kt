package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.resources

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.IntegrationTestBase

class ControllerIntegrationBase : IntegrationTestBase() {
  fun noAuthHeaderRespondsWithUnauthorizedTest(uri: String, post: Boolean = false) {
    val testObject =
      if (post) {
        webTestClient.post()
      } else {
        webTestClient.get()
      }

    testObject
      .uri(uri)
      .exchange()
      .expectStatus()
      .isUnauthorized
  }

  fun noRoleInAuthHeaderRespondsWithForbiddenTest(uri: String, post: Boolean = false) {
    val testObject =
      if (post) {
        webTestClient.post()
      } else {
        webTestClient.get()
      }

    testObject
      .uri(uri)
      .headers(setAuthorisation(roles = emptyList<String>()))
      .exchange()
      .expectStatus()
      .isForbidden
  }

  fun wrongRolesRespondsWithForbiddenTest(uri: String, roles: List<String>, post: Boolean = false) {
    val testObject =
      if (post) {
        webTestClient.post()
      } else {
        webTestClient.get()
      }

    testObject
      .uri(uri)
      .headers(setAuthorisation(roles = roles))
      .exchange()
      .expectStatus()
      .isForbidden
  }
}
