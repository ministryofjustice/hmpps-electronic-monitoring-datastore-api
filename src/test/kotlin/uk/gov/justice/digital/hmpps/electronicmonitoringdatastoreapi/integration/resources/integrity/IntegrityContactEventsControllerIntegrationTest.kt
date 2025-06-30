package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.resources.integrity

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_GENERAL__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_RESTRICTED__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.resources.ControllerIntegrationBase
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockEmDatastoreClient

@ActiveProfiles("integration")
class IntegrityContactEventsControllerIntegrationTest : ControllerIntegrationBase() {

  @Nested
  @DisplayName("GET /orders/integrity/{legacySubjectId}/contact-events")
  inner class GetIntegrityGeneralContactEvents {

    @BeforeEach
    fun setup() {
      MockEmDatastoreClient.addResponseFile("successfulContactEventsResponse")
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/integrity/234/contact-events")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/integrity/234/contact-events")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/integrity/234/contact-events", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/integrity/2_4/contact-events")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_GENERAL__RO)))
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing general orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/contact-events")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_GENERAL__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/contact-events")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }
  }

  @Nested
  @DisplayName("GET /orders/integrity/{legacySubjectId}/contact-events?restricted=true")
  inner class GetIntegrityRestrictedContactEvents {

    @BeforeEach
    fun setup() {
      MockEmDatastoreClient.addResponseFile("successfulContactEventsResponse")
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/integrity/234/contact-events?restricted=true")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/integrity/234/contact-events?restricted=true")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/integrity/234/contact-events?restricted=true", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should return 403 forbidden if general role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/integrity/234/contact-events?restricted=true", listOf(ROLE_EM_DATASTORE_GENERAL__RO))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/integrity/2_4/contact-events?restricted=true")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/contact-events?restricted=true")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }
  }
}
