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
class IntegrityEquipmentDetailsControllerIntegrationTest : ControllerIntegrationBase() {

  @Autowired
  lateinit var cacheEntryRepository: CacheEntryRepository

  @BeforeEach
  fun setup() {
    cacheEntryRepository.deleteAll()
  }

  @Nested
  @DisplayName("GET /orders/integrity/{legacySubjectId}/equipment-details")
  inner class GetGeneralEquipmentDetails {

    @BeforeEach
    fun setup() {
      stubQueryExecution(
        "123",
        1,
        "SUCCEEDED",
        MockAthenaResultSetBuilder(
          mapOf(
            "legacy_subject_id" to "varchar",
            "hmu_id" to "bigint",
            "hmu_equipment_category_description" to "varchar",
            "hmu_install_date" to "date",
            "hmu_install_time" to "varchar",
            "hmu_removed_date" to "date",
            "hmu_removed_time" to "varchar",
            "pid_id" to "bigint",
            "pid_equipment_category_description" to "varchar",
            "date_device_installed" to "date",
            "time_device_installed" to "varchar",
            "date_device_removed" to "date",
            "time_device_removed" to "varchar",
          ),
          arrayOf(
            arrayOf(
              "1401253",
              "976171",
              "GSM HMU",
              "2023-05-30",
              "10:40:00",
              "2023-05-31",
              "09:27:00",
              "653418",
              "PID",
              "2023-05-30",
              "10:40:00",
              "2023-05-31",
              "09:27:00",
            ),
            arrayOf(
              "1401253",
              "932129",
              "GSM HMU",
              "2023-05-16",
              "10:42:00",
              "2023-05-29",
              "10:20:00",
              "239053",
              "PID",
              "2023-05-16",
              "10:42:00",
              "2023-05-29",
              "10:20:00",
            ),
          ),
        ).toString(),
      )
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/integrity/234/equipment-details")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/integrity/234/equipment-details")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/integrity/234/equipment-details", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/integrity/2_4/equipment-details")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_GENERAL__RO)))
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing general orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/equipment-details")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_GENERAL__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/equipment-details")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }
  }

  @Nested
  @DisplayName("GET /orders/integrity/{legacySubjectId}/equipment-details?restricted=true")
  inner class GetRestrictedEquipmentDetails {

    @BeforeEach
    fun setup() {
      stubQueryExecution(
        "123",
        1,
        "SUCCEEDED",
        MockAthenaResultSetBuilder(
          mapOf(
            "legacy_subject_id" to "varchar",
            "hmu_id" to "bigint",
            "hmu_equipment_category_description" to "varchar",
            "hmu_install_date" to "date",
            "hmu_install_time" to "varchar",
            "hmu_removed_date" to "date",
            "hmu_removed_time" to "varchar",
            "pid_id" to "bigint",
            "pid_equipment_category_description" to "varchar",
            "date_device_installed" to "date",
            "time_device_installed" to "varchar",
            "date_device_removed" to "date",
            "time_device_removed" to "varchar",
          ),
          arrayOf(
            arrayOf(
              "1401253",
              "976171",
              "GSM HMU",
              "2023-05-30",
              "10:40:00",
              "2023-05-31",
              "09:27:00",
              "653418",
              "PID",
              "2023-05-30",
              "10:40:00",
              "2023-05-31",
              "09:27:00",
            ),
            arrayOf(
              "1401253",
              "932129",
              "GSM HMU",
              "2023-05-16",
              "10:42:00",
              "2023-05-29",
              "10:20:00",
              "239053",
              "PID",
              "2023-05-16",
              "10:42:00",
              "2023-05-29",
              "10:20:00",
            ),
          ),
        ).toString(),
      )
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/integrity/234/equipment-details?restricted=true")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/integrity/234/equipment-details?restricted=true")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/integrity/234/equipment-details?restricted=true", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should return 403 forbidden if general role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/integrity/234/equipment-details?restricted=true", listOf(ROLE_EM_DATASTORE_GENERAL__RO))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/integrity/2_4/equipment-details?restricted=true")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234/equipment-details?restricted=true")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }
  }
}
