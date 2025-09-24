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
class AlcoholMonitoringIncidentEventsControllerIntegrationTest : ControllerIntegrationBase() {

  @Autowired
  lateinit var cacheEntryRepository: CacheEntryRepository

  @BeforeEach
  fun setup() {
    cacheEntryRepository.deleteAll()
  }

  @Nested
  @DisplayName("GET /orders/alcohol-monitoring/{legacySubjectId}/incident-events")
  inner class GetIncidentEvents {

    @BeforeEach
    fun setup() {
      stubQueryExecution(
        "123",
        1,
        "SUCCEEDED",
        MockAthenaResultSetBuilder(
          mapOf(
            "legacy_subject_id" to "varchar",
            "violation_alert_id" to "varchar",
            "violation_alert_date" to "varchar",
            "violation_alert_time" to "varchar",
            "violation_alert_type" to "varchar",
            "violation_alert_response_action" to "varchar",
            "visit_required" to "varchar",
            "probation_interaction_required" to "varchar",
            "ams_interaction_required" to "varchar",
            "multiple_alerts" to "varchar",
            "additional_alerts" to "varchar",
          ),
          arrayOf(
            arrayOf(
              "1234",
              "V001",
              "2001-01-01",
              "01:01:01",
              "Test type",
              "Test action",
              "Yes",
              "No",
              "Yes",
              "No",
              "Yes",
            ),
            arrayOf(
              "4321",
              "V002",
              "2002-02-02",
              "02:02:02",
              "Test type",
              "Test action",
              "No",
              "Yes",
              "No",
              "Yes",
              "No",
            ),
          ),
        ).toString(),
      )
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/alcohol-monitoring/234/incident-events")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/alcohol-monitoring/234/incident-events")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/alcohol-monitoring/234/incident-events", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/2_4/incident-events")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/234/incident-events")
        .headers(setAuthorisation(roles = listOf("ROLE_EM_DATASTORE_RESTRICTED_RO")))
        .exchange()
        .expectStatus()
        .isOk
    }

    @Test
    fun `should return OK with valid auth header and role for accessing general orders`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/234/incident-events")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
    }
  }
}
