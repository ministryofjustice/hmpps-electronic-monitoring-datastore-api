package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.resources

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.mocks.MockEmDatastoreClient

@ActiveProfiles("integration")
class CurfewTimetableControllerIntegrationTest : ControllerIntegrationBase() {
  @Nested
  @DisplayName("GET /orders/getCurfewTimetable/{orderId}/curfew-timetable")
  inner class GetCurfewTimetable {
    val baseUri = "/orders/getCurfewTimetable"

    @BeforeEach
    fun setup() {
      MockEmDatastoreClient.Companion.addResponseFile("successfulCurfewTimetableResponse")
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
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("$baseUri/2_4")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isBadRequest
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
