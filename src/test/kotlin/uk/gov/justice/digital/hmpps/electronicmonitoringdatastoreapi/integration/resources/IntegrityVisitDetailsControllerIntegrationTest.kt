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
class IntegrityVisitDetailsControllerIntegrationTest : ControllerIntegrationBase() {

  @Autowired
  lateinit var cacheEntryRepository: CacheEntryRepository

  @BeforeEach
  fun setup() {
    cacheEntryRepository.deleteAll()
  }

  @Nested
  @DisplayName("GET /orders/integrity/{legacySubjectId}/visit-details")
  inner class GetGeneralVisitDetails {

    @BeforeEach
    fun setup() {
      stubQueryExecution(
        "123",
        1,
        "SUCCEEDED",
        MockAthenaResultSetBuilder(
          mapOf(
            "legacy_subject_id" to "varchar",
            "address_1" to "varchar",
            "address_2" to "varchar",
            "address_3" to "varchar",
            "postcode" to "varchar",
            "actual_work_start_date" to "varchar",
            "actual_work_start_time" to "varchar",
            "actual_work_end_date" to "varchar",
            "actual_work_end_time" to "varchar",
            "visit_notes" to "varchar",
            "visit_type" to "varchar",
            "visit_outcome" to "varchar",
          ),
          arrayOf(
            arrayOf(
              "1401253",
              "Washington Field Service Centre, Un",
              "Unit 14-16 Vermont House, Vermont R",
              "Washington",
              "NE37 2SQ",
              "2017-08-23",
              "20:02:00",
              "2017-08-23",
              "21:08:00",
              "Visited the specified address to change the subjects equipment. New equipment was installed. IVR confirmed Installation Success.Range test confirmed that the Subjects tag was in range in all areas of the property. (Excluding gardens /balconies)  Range set to:  Skin check carried out/ankle rotation observed.  PID fitted to RIGHT/LEFT leg.  Recovered HMU No:324720  New HMU No:306022  Recovered PID No:187278  New PID No:206258  Security Seal No:086381  Equipment left in good working order. also recovered hmu-27499 subject sister tracy woodward-dervil, is in style prison.",
              "Range Test",
              "Success",
            ),
            arrayOf(
              "1401253",
              "Washington Field Service Centre, Un",
              "Unit 14-16 Vermont House, Vermont R",
              "Washington",
              "NE37 2SQ",
              "2015-11-17",
              "18:46:00",
              "2015-11-17",
              "20:22:00",
              "Outgoing Phone Call for (Left home during curfew Event, Return home late) No Answer",
              "Install",
              "Success",
            ),
          ),
        ).toString(),
      )
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/integrity/234/visit-details")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/integrity/234/visit-details")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/integrity/234/visit-details", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/integrity/2_4/visit-details")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_GENERAL__RO)))
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/visit-details")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }

    @Test
    fun `should return OK with valid auth header and role for accessing general orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/visit-details")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_GENERAL__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }
  }

  @Nested
  @DisplayName("GET /orders/integrity/{legacySubjectId}/visit-details?restricted=true")
  inner class GetRestrictedVisitDetails {

    @BeforeEach
    fun setup() {
      stubQueryExecution(
        "123",
        1,
        "SUCCEEDED",
        MockAthenaResultSetBuilder(
          mapOf(
            "legacy_subject_id" to "varchar",
            "address_1" to "varchar",
            "address_2" to "varchar",
            "address_3" to "varchar",
            "postcode" to "varchar",
            "actual_work_start_date" to "varchar",
            "actual_work_start_time" to "varchar",
            "actual_work_end_date" to "varchar",
            "actual_work_end_time" to "varchar",
            "visit_notes" to "varchar",
            "visit_type" to "varchar",
            "visit_outcome" to "varchar",
          ),
          arrayOf(
            arrayOf(
              "1401253",
              "Washington Field Service Centre, Un",
              "Unit 14-16 Vermont House, Vermont R",
              "Washington",
              "NE37 2SQ",
              "2017-08-23",
              "20:02:00",
              "2017-08-23",
              "21:08:00",
              "Visited the specified address to change the subjects equipment. New equipment was installed. IVR confirmed Installation Success.Range test confirmed that the Subjects tag was in range in all areas of the property. (Excluding gardens /balconies)  Range set to:  Skin check carried out/ankle rotation observed.  PID fitted to RIGHT/LEFT leg.  Recovered HMU No:324720  New HMU No:306022  Recovered PID No:187278  New PID No:206258  Security Seal No:086381  Equipment left in good working order. also recovered hmu-27499 subject sister tracy woodward-dervil, is in style prison.",
              "Range Test",
              "Success",
            ),
            arrayOf(
              "1401253",
              "Washington Field Service Centre, Un",
              "Unit 14-16 Vermont House, Vermont R",
              "Washington",
              "NE37 2SQ",
              "2015-11-17",
              "18:46:00",
              "2015-11-17",
              "20:22:00",
              "Outgoing Phone Call for (Left home during curfew Event, Return home late) No Answer",
              "Install",
              "Success",
            ),
          ),
        ).toString(),
      )
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/integrity/234/visit-details?restricted=true")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/integrity/234/visit-details?restricted=true")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/integrity/234/visit-details?restricted=true", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should return 403 forbidden if general role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/integrity/234/visit-details?restricted=true", listOf(ROLE_EM_DATASTORE_GENERAL__RO))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/integrity/2_4/visit-details?restricted=true")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/visit-details?restricted=true")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }
  }
}
