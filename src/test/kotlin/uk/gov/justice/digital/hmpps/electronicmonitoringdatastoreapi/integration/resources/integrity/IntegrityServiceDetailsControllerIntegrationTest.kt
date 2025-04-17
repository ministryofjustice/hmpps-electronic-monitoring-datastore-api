package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.resources.integrity

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.resources.ControllerIntegrationBase
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.mocks.MockEmDatastoreClient

@ActiveProfiles("integration")
class IntegrityServiceDetailsControllerIntegrationTest : ControllerIntegrationBase() {
  @Nested
  @DisplayName("GET /orders/integrity/{legacySubjectId}/service-details")
  inner class GetServiceDetails {
    @BeforeEach
    fun setup() {
      MockEmDatastoreClient.Companion.addResponseFile("successfulServiceDetailsResponse")
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/integrity/234/service-details")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/integrity/234/service-details")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/integrity/234/service-details", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/integrity/2_4/service-details")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/service-details")
        .headers(setAuthorisation(roles = listOf("ROLE_EM_DATASTORE_RESTRICTED_RO")))
        .exchange()
        .expectStatus()
        .isOk
    }

    @Test
    fun `should return OK with valid auth header and role for accessing general orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/service-details")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
    }
  }
}
