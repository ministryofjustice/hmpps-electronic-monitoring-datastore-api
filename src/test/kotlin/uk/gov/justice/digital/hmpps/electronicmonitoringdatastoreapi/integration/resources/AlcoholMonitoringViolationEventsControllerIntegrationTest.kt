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
class AlcoholMonitoringViolationEventsControllerIntegrationTest : ControllerIntegrationBase() {

  @Autowired
  lateinit var cacheEntryRepository: CacheEntryRepository

  @BeforeEach
  fun setup() {
    cacheEntryRepository.deleteAll()
  }

  @Nested
  @DisplayName("GET /orders/alcohol-monitoring/{legacySubjectId}/violation-events")
  inner class GetViolationEvents {

    @BeforeEach
    fun setup() {
      stubQueryExecution(
        "123",
        1,
        "SUCCEEDED",
        MockAthenaResultSetBuilder(
          mapOf(
            "legacy_subject_id" to "varchar",
            "enforcement_id" to "varchar",
            "non_compliance_reason" to "varchar",
            "non_compliance_date" to "varchar",
            "non_compliance_time" to "varchar",
            "violation_alert_id" to "varchar",
            "violation_alert_description" to "varchar",
            "violation_event_notification_date" to "varchar",
            "violation_event_notification_time" to "varchar",
            "action_taken_ems" to "varchar",
            "non_compliance_outcome" to "varchar",
            "non_compliance_resolved" to "varchar",
            "date_resolved" to "varchar",
            "open_closed" to "varchar",
            "visit_required" to "varchar",
          ),
          arrayOf(
            arrayOf(
              "1234",
              "E001",
              "Test reason",
              "2001-01-01",
              "01:01:01",
              "V001",
              "Test alert description",
              "2002-02-02",
              "02:02:02",
              "Test action taken EMS",
              "Test noncompliance outcome",
              "Yes",
              "2003-03-03",
              "Closed",
              "No",
            ),
            arrayOf(
              "4321",
              "E002",
              "Test reason",
              "2004-04-04",
              "04:04:04",
              "V002",
              "Test alert description",
              "2005-05-05",
              "05:05:05",
              "Test action taken EMS",
              "Test noncompliance outcome",
              "No",
              "2006-06-06",
              "Open",
              "Yes",
            ),
          ),
        ).toString(),
      )
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/alcohol-monitoring/234/violation-events")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/alcohol-monitoring/234/violation-events")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/alcohol-monitoring/234/violation-events", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/2_4/violation-events")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/234/violation-events")
        .headers(setAuthorisation(roles = listOf("ROLE_EM_DATASTORE_RESTRICTED_RO")))
        .exchange()
        .expectStatus()
        .isOk
    }

    @Test
    fun `should return OK with valid auth header and role for accessing general orders`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/234/violation-events")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
    }
  }
}
