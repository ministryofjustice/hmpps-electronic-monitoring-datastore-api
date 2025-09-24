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
class AlcoholMonitoringVisitDetailsControllerIntegrationTest : ControllerIntegrationBase() {

  @Autowired
  lateinit var cacheEntryRepository: CacheEntryRepository

  @BeforeEach
  fun setup() {
    cacheEntryRepository.deleteAll()
  }

  @Nested
  @DisplayName("GET /orders/alcohol-monitoring/{legacySubjectId}/visit-details")
  inner class GetVisitDetails {

    @BeforeEach
    fun setup() {
      stubQueryExecution(
        "123",
        1,
        "SUCCEEDED",
        MockAthenaResultSetBuilder(
          mapOf(
            "legacy_subject_id" to "varchar",
            "visit_id" to "varchar",
            "visit_type" to "varchar",
            "visit_attempt" to "varchar",
            "date_visit_raised" to "varchar",
            "visit_address" to "varchar",
            "visit_notes" to "varchar",
            "visit_outcome" to "varchar",
            "actual_work_start_date" to "varchar",
            "actual_work_start_time" to "varchar",
            "actual_work_end_date" to "varchar",
            "actual_work_end_time" to "varchar",
            "visit_rejection_reason" to "varchar",
            "visit_rejection_description" to "varchar",
            "visit_cancel_reason" to "varchar",
            "visit_cancel_description" to "varchar",
          ),
          arrayOf(
            arrayOf(
              "333",
              "999",
              "Community",
              "Attempt 7",
              "2011-02-04",
              "9 Cloud Lane CL0 3DS",
              "Notes about the visit",
              "Visit outcome",
              "2011-02-05",
              "12:00:15",
              "2011-02-05",
              "12:34:29",
              "",
              "",
              "",
              "",
            ),
            arrayOf(
              "333",
              "1000",
              "Community",
              "Attempt 8",
              "2011-02-05",
              "9 Cloud Lane CL0 3DS",
              "Notes about the visit",
              "Visit outcome",
              "2011-02-06",
              "16:20:00",
              "2011-02-06",
              "17:09:45",
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
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/alcohol-monitoring/234/visit-details")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/alcohol-monitoring/234/visit-details")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/alcohol-monitoring/234/visit-details", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/2_4/visit-details")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/234/visit-details")
        .headers(setAuthorisation(roles = listOf("ROLE_EM_DATASTORE_RESTRICTED_RO")))
        .exchange()
        .expectStatus()
        .isOk
    }

    @Test
    fun `should return OK with valid auth header and role for accessing general orders`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/234/visit-details")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
    }
  }
}
