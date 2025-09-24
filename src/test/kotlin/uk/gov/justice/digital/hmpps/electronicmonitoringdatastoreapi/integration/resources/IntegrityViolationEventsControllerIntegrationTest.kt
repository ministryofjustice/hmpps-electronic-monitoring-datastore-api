package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.resources

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.caching.CacheEntryRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder

@ActiveProfiles("integration")
class IntegrityViolationEventsControllerIntegrationTest : ControllerIntegrationBase() {

  @Autowired
  lateinit var cacheEntryRepository: CacheEntryRepository

  @BeforeEach
  fun setup() {
    cacheEntryRepository.deleteAll()
  }

  @Nested
  @DisplayName("GET /orders/integrity/{orderId}/violation-events")
  inner class GetGeneralViolationEvents {

    @BeforeEach
    fun setup() {
      stubQueryExecution(
        "123",
        1,
        "SUCCEEDED",
        MockAthenaResultSetBuilder(
          mapOf(
            "legacy_subject_id" to "varchar",
            "enforcement_reason" to "varchar",
            "investigation_outcome_reason" to "varchar",
            "breach_details" to "varchar",
            "breach_enforcement_outcome" to "varchar",
            "agency_action" to "varchar",
            "breach_date" to "varchar",
            "breach_time" to "varchar",
            "breach_identified_date" to "varchar",
            "breach_identified_time" to "varchar",
            "authority_first_notified_date" to "varchar",
            "authority_first_notified_time" to "varchar",
            "agency_response_date" to "varchar",
            "breach_pack_requested_date" to "varchar",
            "breach_pack_sent_date" to "varchar",
            "section_9_date" to "varchar",
            "hearing_date" to "varchar",
            "summons_served_date" to "varchar",
            "subject_letter_sent_date" to "varchar",
            "warning_letter_sent_date" to "varchar",
            "warning_letter_sent_time" to "varchar",
          ),
          arrayOf(
            arrayOf(
              "1401253",
              "15 Minute Absence",
              "15 minute Absence",
              "Breach",
              "Pending",
              "No Value",
              "2020-12-09",
              "00:00:00",
              "2022-09-16",
              "10:45:00",
              "2022-09-16",
              "08:00:00",
              "",
              "",
              "",
              "",
              "",
              "",
              "",
              "",
              "",
            ),
            arrayOf(
              "1401253",
              "15 Minute Absence",
              "15 minute Absence",
              "Breach",
              "Pending",
              "No Value",
              "2020-12-09",
              "00:00:00",
              "2022-09-16",
              "11:00:00",
              "2022-09-16",
              "08:00:00",
              "",
              "",
              "",
              "",
              "",
              "",
              "",
              "",
              "",
            ),
          ),
        ).toString(),
      )
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/integrity/234/violation-events")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/integrity/234/violation-events")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/integrity/234/violation-events", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/integrity/2_4/violation-events")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/violation-events")
        .headers(setAuthorisation(roles = listOf("ROLE_EM_DATASTORE_RESTRICTED_RO")))
        .exchange()
        .expectStatus()
        .isOk
    }

    @Test
    fun `should return OK with valid auth header and role for accessing general orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/violation-events")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
    }
  }

  @Nested
  @DisplayName("GET /orders/integrity/{orderId}/violation-events?restricted=true")
  inner class GetRestrictedViolationEvents {

    @BeforeEach
    fun setup() {
      stubQueryExecution(
        "123",
        1,
        "SUCCEEDED",
        MockAthenaResultSetBuilder(
          mapOf(
            "legacy_subject_id" to "varchar",
            "enforcement_reason" to "varchar",
            "investigation_outcome_reason" to "varchar",
            "breach_details" to "varchar",
            "breach_enforcement_outcome" to "varchar",
            "agency_action" to "varchar",
            "breach_date" to "varchar",
            "breach_time" to "varchar",
            "breach_identified_date" to "varchar",
            "breach_identified_time" to "varchar",
            "authority_first_notified_date" to "varchar",
            "authority_first_notified_time" to "varchar",
            "agency_response_date" to "varchar",
            "breach_pack_requested_date" to "varchar",
            "breach_pack_sent_date" to "varchar",
            "section_9_date" to "varchar",
            "hearing_date" to "varchar",
            "summons_served_date" to "varchar",
            "subject_letter_sent_date" to "varchar",
            "warning_letter_sent_date" to "varchar",
            "warning_letter_sent_time" to "varchar",
          ),
          arrayOf(
            arrayOf(
              "1401253",
              "15 Minute Absence",
              "15 minute Absence",
              "Breach",
              "Pending",
              "No Value",
              "2020-12-09",
              "00:00:00",
              "2022-09-16",
              "10:45:00",
              "2022-09-16",
              "08:00:00",
              "",
              "",
              "",
              "",
              "",
              "",
              "",
              "",
              "",
            ),
            arrayOf(
              "1401253",
              "15 Minute Absence",
              "15 minute Absence",
              "Breach",
              "Pending",
              "No Value",
              "2020-12-09",
              "00:00:00",
              "2022-09-16",
              "11:00:00",
              "2022-09-16",
              "08:00:00",
              "",
              "",
              "",
              "",
              "",
              "",
              "",
              "",
              "",
            ),
          ),
        ).toString(),
      )
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/integrity/234/violation-events?restricted=true")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/integrity/234/violation-events?restricted=true")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/integrity/234/violation-events?restricted=true", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/integrity/2_4/violation-events?restricted=true")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/violation-events?restricted=true")
        .headers(setAuthorisation(roles = listOf("ROLE_EM_DATASTORE_RESTRICTED_RO")))
        .exchange()
        .expectStatus()
        .isOk
    }

    @Test
    fun `should return 403 FORBIDDEN with valid auth header and role for accessing general orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/violation-events?restricted=true")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isForbidden
    }
  }
}
