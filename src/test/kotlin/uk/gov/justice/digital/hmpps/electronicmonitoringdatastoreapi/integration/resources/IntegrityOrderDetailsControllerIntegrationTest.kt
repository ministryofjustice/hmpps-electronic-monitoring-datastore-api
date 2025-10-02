package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.resources

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_GENERAL__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_RESTRICTED__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder
import java.util.UUID

@ActiveProfiles("integration")
class IntegrityOrderDetailsControllerIntegrationTest : ControllerIntegrationBase() {

  @Nested
  @DisplayName("POST /orders/integrity")
  inner class SearchOrders {
    val queryExecutionId = UUID.randomUUID().toString()

    @BeforeEach
    fun setup() {
      stubGetQueryExecutionId(
        queryExecutionId,
        1,
        "SUCCEEDED",
      )
    }

    @Test
    fun `should fail with 401 when no authorization header is provided`() {
      val requestBody = mapOf(
        "legacySubjectId" to "12345",
      )

      webTestClient.post()
        .uri("/orders/integrity")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should fail with 403 when user has no required roles`() {
      val requestBody = mapOf(
        "legacySubjectId" to "12345",
      )

      webTestClient.post()
        .uri("/orders/integrity")
        .headers(setAuthorisation(roles = listOf()))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should fail with 400 BAD REQUEST if empty body`() {
      webTestClient.post()
        .uri("/orders/integrity")
        .headers(setAuthorisation())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{}")
        .exchange()
        .expectStatus()
        .is4xxClientError
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.userMessage").isEqualTo("Validation failure: This request is malformed, there must be at least one search criteria present")
    }

    @Test
    fun `should fail with 400 BAD REQUEST if invalid criteria`() {
      val requestBody = mapOf(
        "legacySubjectId" to "12345",
        "firstName" to "12345",
        "lastName" to "Smith",
      )

      webTestClient.post()
        .uri("/orders/integrity")
        .headers(setAuthorisation())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .exchange()
        .expectStatus()
        .is4xxClientError
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.userMessage").value<String> {
          assertThat(it).contains("Field error in object 'orderSearchCriteria' on field 'firstName'")
        }
    }

    @Test
    fun `should return 200 with valid body and integrity search type`() {
      val requestBody = mapOf(
        "legacySubjectId" to "12345",
        "firstName" to "Pheobe",
        "lastName" to "Smith",
      )

      webTestClient.post()
        .uri("/orders/integrity")
        .headers(setAuthorisation())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .exchange()
        .expectStatus()
        .isOk
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.queryExecutionId").isEqualTo(queryExecutionId)
    }
  }

  @Nested
  @DisplayName("GET /orders/integrity?id={queryExecutionId}")
  inner class GetIntegritySearchResults {

    @BeforeEach
    fun setup() {
      stubQueryExecution(
        UUID.randomUUID().toString(),
        1,
        "SUCCEEDED",
        MockAthenaResultSetBuilder(
          mapOf(
            "legacy_subject_id" to "varchar",
            "legacy_order_id" to "varchar",
            "first_name" to "varchar",
            "last_name" to "varchar",
            "alias" to "varchar",
            "date_of_birth" to "date",
            "adult_or_child" to "varchar",
            "sex" to "varchar",
            "contact" to "varchar",
            "primary_address_line_1" to "varchar",
            "primary_address_line_2" to "varchar",
            "primary_address_line_3" to "varchar",
            "primary_address_post_code" to "varchar",
            "phone_or_mobile_number" to "varchar",
            "ppo" to "varchar",
            "mappa" to "varchar",
            "technical_bail" to "varchar",
            "manual_risk" to "varchar",
            "offence_risk" to "boolean",
            "post_code_risk" to "varchar",
            "false_limb_risk" to "varchar",
            "migrated_risk" to "varchar",
            "range_risk" to "varchar",
            "report_risk" to "varchar",
            "order_start_date" to "date",
            "order_end_date" to "date",
            "order_type" to "varchar",
            "order_type_description" to "varchar",
            "order_type_detail" to "varchar",
            "wearing_wrist_pid" to "varchar",
            "notifying_organisation_details_name" to "varchar",
            "responsible_organisation" to "varchar",
            "responsible_organisation_details_region" to "varchar",
          ),
          arrayOf(
            arrayOf(
              "1000000",
              "987654321",
              "Pheobe",
              "Smith",
              "",
              "1991-06-15",
              "Adult",
              "Male",
              "",
              "Unit 1, Lamby Workshops, Lamby Way, Rumney",
              "Lamby Way, Rumney",
              "Cardiff",
              "CF3 2EQ",
              "",
              "No",
              "No Mappa",
              "No",
              "",
              "false",
              "CF3 2EQ",
              "",
              "",
              "",
              "",
              "2022-05-19",
              "2025-01-01",
              "Pre-Trial",
              "GPS Bail",
              "Curfew (Without Location Tracking)",
              "No",
              "Manchester and Salford Magistrates Court",
              "",
              "No Organisation Identified",
            ),
            arrayOf(
              "2000000",
              "987654321",
              "Bill",
              "Smith",
              "",
              "1991-06-15",
              "Adult",
              "Male",
              "",
              "Unit 1, Lamby Workshops, Lamby Way, Rumney",
              "Lamby Way, Rumney",
              "Cardiff",
              "CF3 2EQ",
              "",
              "No",
              "No Mappa",
              "No",
              "",
              "false",
              "CF3 2EQ",
              "",
              "",
              "",
              "",
              "2022-05-19",
              "2025-01-01",
              "Pre-Trial",
              "GPS Bail",
              "Curfew (Without Location Tracking)",
              "No",
              "Manchester and Salford Magistrates Court",
              "",
              "No Organisation Identified",
            ),
            arrayOf(
              "3000000",
              "987654321",
              "Claire",
              "Smith",
              "",
              "1991-06-15",
              "Adult",
              "Male",
              "",
              "Unit 1, Lamby Workshops, Lamby Way, Rumney",
              "Lamby Way, Rumney",
              "Cardiff",
              "CF3 2EQ",
              "",
              "No",
              "No Mappa",
              "No",
              "",
              "false",
              "CF3 2EQ",
              "",
              "",
              "",
              "",
              "2022-05-19",
              "2025-01-01",
              "Pre-Trial",
              "GPS Bail",
              "Curfew (Without Location Tracking)",
              "No",
              "Manchester and Salford Magistrates Court",
              "",
              "No Organisation Identified",
            ),
            arrayOf(
              "8000000",
              "987654321",
              "Daniel",
              "Smith",
              "",
              "1991-06-15",
              "Adult",
              "Male",
              "",
              "Unit 1, Lamby Workshops, Lamby Way, Rumney",
              "Lamby Way, Rumney",
              "Cardiff",
              "CF3 2EQ",
              "",
              "No",
              "No Mappa",
              "No",
              "",
              "false",
              "CF3 2EQ",
              "",
              "",
              "",
              "",
              "2022-05-19",
              "2025-01-01",
              "Pre-Trial",
              "GPS Bail",
              "Curfew (Without Location Tracking)",
              "No",
              "Manchester and Salford Magistrates Court",
              "",
              "No Organisation Identified",
            ),
            arrayOf(
              "30000",
              "987654321",
              "Emma",
              "Smith",
              "",
              "1991-06-15",
              "Adult",
              "Male",
              "",
              "Unit 1, Lamby Workshops, Lamby Way, Rumney",
              "Lamby Way, Rumney",
              "Cardiff",
              "CF3 2EQ",
              "",
              "No",
              "No Mappa",
              "No",
              "",
              "false",
              "CF3 2EQ",
              "",
              "",
              "",
              "",
              "2022-05-19",
              "2025-01-01",
              "Pre-Trial",
              "GPS Bail",
              "Curfew (Without Location Tracking)",
              "No",
              "Manchester and Salford Magistrates Court",
              "",
              "No Organisation Identified",
            ),
            arrayOf(
              "4000000",
              "987654321",
              "Fred",
              "Smith",
              "",
              "1991-06-15",
              "Adult",
              "Male",
              "",
              "Unit 1, Lamby Workshops, Lamby Way, Rumney",
              "Lamby Way, Rumney",
              "Cardiff",
              "CF3 2EQ",
              "",
              "No",
              "No Mappa",
              "No",
              "",
              "false",
              "CF3 2EQ",
              "",
              "",
              "",
              "",
              "2022-05-19",
              "2025-01-01",
              "Pre-Trial",
              "GPS Bail",
              "Curfew (Without Location Tracking)",
              "No",
              "Manchester and Salford Magistrates Court",
              "",
              "No Organisation Identified",
            ),
          ),
        ).toString(),
      )
    }

    @Test
    fun `should fail with 401 when no authorization header is provided`() {
      webTestClient.get()
        .uri("/orders/integrity?id=HT-12345")
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should fail with 403 when user has no required roles`() {
      webTestClient.get()
        .uri("/orders/integrity?id=HT-12345")
        .headers(setAuthorisation(roles = listOf()))
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should return 200 with valid configuration`() {
      webTestClient.get()
        .uri("/orders/integrity?id=HT-12345")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.length()").isEqualTo(6)
        .jsonPath("$[0].firstName").isEqualTo("Pheobe")
        .jsonPath("$[0].lastName").isEqualTo("Smith")
    }

    @Test
    fun `should return 500 with missing query execution id`() {
      webTestClient.get()
        .uri("/orders/integrity")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .is5xxServerError
    }
  }

  @Nested
  @DisplayName("GET /orders/integrity/{legacySubjectId}")
  inner class GetIntegrityGeneralOrderDetails {

    @BeforeEach
    fun setup() {
      stubQueryExecution(
        UUID.randomUUID().toString(),
        1,
        "SUCCEEDED",
        MockAthenaResultSetBuilder(
          mapOf(
            "legacy_subject_id" to "varchar",
            "legacy_order_id" to "varchar",
            "first_name" to "varchar",
            "last_name" to "varchar",
            "alias" to "varchar",
            "date_of_birth" to "date",
            "adult_or_child" to "varchar",
            "sex" to "varchar",
            "contact" to "varchar",
            "primary_address_line_1" to "varchar",
            "primary_address_line_2" to "varchar",
            "primary_address_line_3" to "varchar",
            "primary_address_post_code" to "varchar",
            "phone_or_mobile_number" to "varchar",
            "ppo" to "varchar",
            "mappa" to "varchar",
            "technical_bail" to "varchar",
            "manual_risk" to "varchar",
            "offence_risk" to "boolean",
            "post_code_risk" to "varchar",
            "false_limb_risk" to "varchar",
            "migrated_risk" to "varchar",
            "range_risk" to "varchar",
            "report_risk" to "varchar",
            "order_start_date" to "date",
            "order_end_date" to "date",
            "order_type" to "varchar",
            "order_type_description" to "varchar",
            "order_type_detail" to "varchar",
            "wearing_wrist_pid" to "varchar",
            "notifying_organisation_details_name" to "varchar",
            "responsible_organisation" to "varchar",
            "responsible_organisation_details_region" to "varchar",
          ),
          arrayOf(
            arrayOf(
              "1401253",
              "1391815",
              "GPS-BAIL-CNLT",
              "EMS-C3126262-GOLIVE",
              "",
              "1991-06-15",
              "Adult",
              "Male",
              "",
              "Unit 1, Lamby Workshops, Lamby Way, Rumney",
              "Lamby Way, Rumney",
              "Cardiff",
              "CF3 2EQ",
              "",
              "No",
              "No Mappa",
              "No",
              "",
              "false",
              "CF3 2EQ",
              "",
              "",
              "",
              "",
              "2022-05-19",
              "2025-01-01",
              "Pre-Trial",
              "GPS Bail",
              "Curfew (Without Location Tracking)",
              "No",
              "Manchester and Salford Magistrates Court",
              "",
              "No Organisation Identified",
            ),
          ),
        ).toString(),
      )
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/integrity/234")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/integrity/234")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/integrity/234", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/integrity/2_4")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_GENERAL__RO)))
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing general orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_GENERAL__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }
  }

  @Nested
  @DisplayName("GET /orders/integrity/{legacySubjectId}?restricted=true")
  inner class GetIntegrityRestrictedOrderDetails {

    @BeforeEach
    fun setup() {
      stubQueryExecution(
        UUID.randomUUID().toString(),
        1,
        "SUCCEEDED",
        MockAthenaResultSetBuilder(
          mapOf(
            "legacy_subject_id" to "varchar",
            "legacy_order_id" to "varchar",
            "first_name" to "varchar",
            "last_name" to "varchar",
            "alias" to "varchar",
            "date_of_birth" to "date",
            "adult_or_child" to "varchar",
            "sex" to "varchar",
            "contact" to "varchar",
            "primary_address_line_1" to "varchar",
            "primary_address_line_2" to "varchar",
            "primary_address_line_3" to "varchar",
            "primary_address_post_code" to "varchar",
            "phone_or_mobile_number" to "varchar",
            "ppo" to "varchar",
            "mappa" to "varchar",
            "technical_bail" to "varchar",
            "manual_risk" to "varchar",
            "offence_risk" to "boolean",
            "post_code_risk" to "varchar",
            "false_limb_risk" to "varchar",
            "migrated_risk" to "varchar",
            "range_risk" to "varchar",
            "report_risk" to "varchar",
            "order_start_date" to "date",
            "order_end_date" to "date",
            "order_type" to "varchar",
            "order_type_description" to "varchar",
            "order_type_detail" to "varchar",
            "wearing_wrist_pid" to "varchar",
            "notifying_organisation_details_name" to "varchar",
            "responsible_organisation" to "varchar",
            "responsible_organisation_details_region" to "varchar",
          ),
          arrayOf(
            arrayOf(
              "1401253",
              "1391815",
              "GPS-BAIL-CNLT",
              "EMS-C3126262-GOLIVE",
              "",
              "1991-06-15",
              "Adult",
              "Male",
              "",
              "Unit 1, Lamby Workshops, Lamby Way, Rumney",
              "Lamby Way, Rumney",
              "Cardiff",
              "CF3 2EQ",
              "",
              "No",
              "No Mappa",
              "No",
              "",
              "false",
              "CF3 2EQ",
              "",
              "",
              "",
              "",
              "2022-05-19",
              "2025-01-01",
              "Pre-Trial",
              "GPS Bail",
              "Curfew (Without Location Tracking)",
              "No",
              "Manchester and Salford Magistrates Court",
              "",
              "No Organisation Identified",
            ),
          ),
        ).toString(),
      )
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/integrity/234?restricted=true")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/integrity/234?restricted=true")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/integrity/234?restricted=true", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should return 403 forbidden if general role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/integrity/234?restricted=true", listOf(ROLE_EM_DATASTORE_GENERAL__RO))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/integrity/2_4?restricted=true")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/integrity/234?restricted=true")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }
  }
}
