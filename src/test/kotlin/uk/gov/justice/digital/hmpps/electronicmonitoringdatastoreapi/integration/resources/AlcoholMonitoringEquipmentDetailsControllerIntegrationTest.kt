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
class AlcoholMonitoringEquipmentDetailsControllerIntegrationTest : ControllerIntegrationBase() {

  @Autowired
  lateinit var cacheEntryRepository: CacheEntryRepository

  @BeforeEach
  fun setup() {
    cacheEntryRepository.deleteAll()
  }

  @Nested
  @DisplayName("GET /orders/alcohol-monitoring/{legacySubjectId}/equipment-details")
  inner class GetEquipmentDetails {

    @BeforeEach
    fun setup() {
      stubQueryExecution(
        "123",
        1,
        "SUCCEEDED",
        MockAthenaResultSetBuilder(
          mapOf(
            "legacy_subject_id" to "varchar",
            "device_type" to "varchar",
            "device_serial_number" to "varchar",
            "device_address_type" to "varchar",
            "leg_fitting" to "varchar",
            "date_device_installed" to "varchar",
            "time_device_installed" to "varchar",
            "date_device_removed" to "varchar",
            "time_device_removed" to "varchar",
            "hmu_install_date" to "varchar",
            "hmu_install_time" to "varchar",
            "hmu_removed_date" to "varchar",
            "hmu_removed_time" to "varchar",
          ),
          arrayOf(
            arrayOf(
              "333",
              "tag",
              "777",
              "primary",
              "left",
              "2011-01-11",
              "11:11:11",
              "2012-02-12",
              "12:12:12",
              "",
              "",
              "",
              "",
            ),
            arrayOf(
              "222",
              "hmu",
              "888",
              "secondary",
              "right",
              "",
              "",
              "",
              "",
              "2013-03-13",
              "13:13:13",
              "2014-04-14",
              "14:14:14",
            ),
          ),
        ).toString(),
      )
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/alcohol-monitoring/234/equipment-details")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/alcohol-monitoring/234/equipment-details")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/alcohol-monitoring/234/equipment-details", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/2_4/equipment-details")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/234/equipment-details")
        .headers(setAuthorisation(roles = listOf("ROLE_EM_DATASTORE_RESTRICTED_RO")))
        .exchange()
        .expectStatus()
        .isOk
    }

    @Test
    fun `should return OK with valid auth header and role for accessing general orders`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/234/equipment-details")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
    }
  }
}
