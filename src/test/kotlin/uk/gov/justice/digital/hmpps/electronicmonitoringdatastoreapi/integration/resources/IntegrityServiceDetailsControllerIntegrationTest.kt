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
class IntegrityServiceDetailsControllerIntegrationTest : ControllerIntegrationBase() {

  @Autowired
  lateinit var cacheEntryRepository: CacheEntryRepository

  @BeforeEach
  fun setup() {
    cacheEntryRepository.deleteAll()
  }

  @Nested
  @DisplayName("GET /orders/integrity/{legacySubjectId}/service-details")
  inner class GetGeneralServiceDetails {

    @BeforeEach
    fun setup() {
      stubQueryExecution(
        "123",
        1,
        "SUCCEEDED",
        MockAthenaResultSetBuilder(
          mapOf(
            "legacy_subject_id" to "varchar",
            "service_id" to "bigint",
            "service_address_1" to "varchar",
            "service_address_2" to "varchar",
            "service_address_3" to "varchar",
            "service_address_postcode" to "varchar",
            "service_start_date" to "date",
            "service_end_date" to "date",
            "curfew_start_date" to "date",
            "curfew_end_date" to "date",
            "curfew_start_time" to "varchar",
            "curfew_end_time" to "varchar",
            "monday" to "bigint",
            "tuesday" to "bigint",
            "wednesday" to "bigint",
            "thursday" to "bigint",
            "friday" to "bigint",
            "saturday" to "bigint",
            "sunday" to "bigint",
          ),
          arrayOf(
            arrayOf(
              "1401253",
              "2390735",
              "Unit 1, Lamby Workshops, Lamby Way, Rumney",
              "Lamby Way, Rumney",
              "Cardiff",
              "CF3 2EQ",
              "2022-05-19",
              "2025-01-01",
              "2022-05-19",
              "2024-05-19",
              "11:30:00",
              "18:00:00",
              "1",
              "1",
              "1",
              "1",
              "1",
              "1",
              "1",
            ),
            arrayOf(
              "1401253",
              "2390734",
              "Unit 1, Lamby Workshops, Lamby Way, Rumney",
              "Lamby Way, Rumney",
              "Cardiff",
              "CF3 2EQ",
              "2022-05-19",
              "2025-01-01",
              "2022-05-19",
              "2024-05-19",
              "11:30:00",
              "18:00:00",
              "1",
              "1",
              "1",
              "1",
              "1",
              "1",
              "1",
            ),
          ),
        ).toString(),
      )
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
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_GENERAL__RO)))
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/service-details")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }

    @Test
    fun `should return OK with valid auth header and role for accessing general orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/service-details")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_GENERAL__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }
  }

  @Nested
  @DisplayName("GET /orders/integrity/{legacySubjectId}/service-details?restricted=true")
  inner class GetRestrictedServiceDetails {

    @BeforeEach
    fun setup() {
      stubQueryExecution(
        "123",
        1,
        "SUCCEEDED",
        MockAthenaResultSetBuilder(
          mapOf(
            "legacy_subject_id" to "varchar",
            "service_id" to "bigint",
            "service_address_1" to "varchar",
            "service_address_2" to "varchar",
            "service_address_3" to "varchar",
            "service_address_postcode" to "varchar",
            "service_start_date" to "date",
            "service_end_date" to "date",
            "curfew_start_date" to "date",
            "curfew_end_date" to "date",
            "curfew_start_time" to "varchar",
            "curfew_end_time" to "varchar",
            "monday" to "bigint",
            "tuesday" to "bigint",
            "wednesday" to "bigint",
            "thursday" to "bigint",
            "friday" to "bigint",
            "saturday" to "bigint",
            "sunday" to "bigint",
          ),
          arrayOf(
            arrayOf(
              "1401253",
              "2390735",
              "Unit 1, Lamby Workshops, Lamby Way, Rumney",
              "Lamby Way, Rumney",
              "Cardiff",
              "CF3 2EQ",
              "2022-05-19",
              "2025-01-01",
              "2022-05-19",
              "2024-05-19",
              "11:30:00",
              "18:00:00",
              "1",
              "1",
              "1",
              "1",
              "1",
              "1",
              "1",
            ),
            arrayOf(
              "1401253",
              "2390734",
              "Unit 1, Lamby Workshops, Lamby Way, Rumney",
              "Lamby Way, Rumney",
              "Cardiff",
              "CF3 2EQ",
              "2022-05-19",
              "2025-01-01",
              "2022-05-19",
              "2024-05-19",
              "11:30:00",
              "18:00:00",
              "1",
              "1",
              "1",
              "1",
              "1",
              "1",
              "1",
            ),
          ),
        ).toString(),
      )
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/integrity/234/service-details?restricted=true")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/integrity/234/service-details?restricted=true")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/integrity/234/service-details?restricted=true", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should return 403 forbidden if general role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/integrity/234/service-details?restricted=true", listOf(ROLE_EM_DATASTORE_GENERAL__RO))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/integrity/2_4/service-details?restricted=true")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/service-details?restricted=true")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }
  }
}
