package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.resources

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_GENERAL__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_RESTRICTED__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.caching.CacheEntryRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder

@ActiveProfiles("integration")
class AlcoholMonitoringContactEventsControllerIntegrationTest : ControllerIntegrationBase() {

  @Autowired
  lateinit var cacheEntryRepository: CacheEntryRepository

  @BeforeEach
  fun setup() {
    cacheEntryRepository.deleteAll()
  }

  @Nested
  @DisplayName("GET /orders/alcohol-monitoring/{legacySubjectId}/contact-events")
  inner class GetContactEvents {

    @BeforeEach
    fun setup() {
      stubQueryExecution(
        "123",
        1,
        "SUCCEEDED",
        MockAthenaResultSetBuilder(
          mapOf(
            "legacy_subject_id" to "varchar",
            "contact_date" to "varchar",
            "contact_time" to "varchar",
            "inbound_or_outbound" to "varchar",
            "from_to" to "varchar",
            "channel" to "varchar",
            "subject_consent_withdrawn" to "varchar",
            "call_outcome" to "varchar",
            "statement" to "varchar",
            "reason_for_contact" to "varchar",
            "outcome_of_contact" to "varchar",
            "visit_required" to "varchar",
            "visit_id" to "varchar",
          ),
          arrayOf(
            arrayOf(
              "1234",
              "2001-01-01",
              "01:01:01",
              "Inbound",
              "Probation",
              "Call",
              "No",
              "Test outcome",
              "Test statement",
              "Test reason for contact",
              "Test outcome of contact",
              "No",
              "V1234",
            ),
            arrayOf(
              "4321",
              "2002-02-02",
              "02:02:02",
              "Outbound",
              "Probation",
              "Call",
              "Yes",
              "Test outcome",
              "Test statement",
              "Test reason for contact",
              "Test outcome of contact",
              "Yes",
              "V4321",
            ),
          ),
        ).toString(),
      )
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/alcohol-monitoring/234/contact-events")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/alcohol-monitoring/234/contact-events")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/alcohol-monitoring/234/contact-events", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/2_4/contact-events")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/234/contact-events")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }

    @Test
    fun `should return OK with valid auth header and role for accessing general orders`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/234/contact-events")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_GENERAL__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }
  }
}
