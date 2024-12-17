package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.controllers

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.IntegrationTestBase

class ControllerIntegrationBase : IntegrationTestBase() {
  fun noAuthHeaderTest(uri: String, post: Boolean = false) {
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

  fun noRoleInAuthHeaderTest(uri: String, post: Boolean = false) {
    val testObject =
      if (post) {
        webTestClient.post()
      } else {
        webTestClient.get()
      }

    testObject
      .uri(uri)
      .headers(setAuthorisation())
      .exchange()
      .expectStatus()
      .isUnauthorized
  }

  fun wrongRolesTest(uri: String, roles: List<String>, post: Boolean = false) {
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
      .isUnauthorized
  }
}
