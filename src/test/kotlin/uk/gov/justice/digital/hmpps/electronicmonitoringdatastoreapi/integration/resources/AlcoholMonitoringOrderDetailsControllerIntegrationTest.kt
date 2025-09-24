package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.resources

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_RESTRICTED__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.caching.CacheEntryRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder
import java.util.UUID

@ActiveProfiles("integration")
class AlcoholMonitoringOrderDetailsControllerIntegrationTest : ControllerIntegrationBase() {

  @Autowired
  lateinit var cacheEntryRepository: CacheEntryRepository

  @BeforeEach
  fun setup() {
    cacheEntryRepository.deleteAll()
  }

  @Nested
  @DisplayName("POST /orders/alcohol-monitoring")
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
        "lastName" to "Smith",
      )

      webTestClient.post()
        .uri("/orders/alcohol-monitoring")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should fail with 403 when user has no required roles`() {
      val requestBody = mapOf(
        "lastName" to "Smith",
      )

      webTestClient.post()
        .uri("/orders/alcohol-monitoring")
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
        .uri("/orders/alcohol-monitoring")
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
        .uri("/orders/alcohol-monitoring")
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
    fun `should return 200 with valid criteria`() {
      val requestBody = mapOf(
        "legacySubjectId" to "12345",
        "firstName" to "Pheobe",
        "lastName" to "Smith",
      )

      webTestClient.post()
        .uri("/orders/alcohol-monitoring")
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
  @DisplayName("GET /orders/alcohol-monitoring?id={queryExecutionId}")
  inner class GetAlcoholMonitoringSearchResults {

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
            "date_of_birth" to "varchar",
            "legacy_gender" to "varchar",
            "special_instructions" to "varchar",
            "phone_or_mobile_number" to "varchar",
            "primary_address_line_1" to "varchar",
            "primary_address_line_2" to "varchar",
            "primary_address_line_3" to "varchar",
            "primary_address_post_code" to "varchar",
            "order_start_date" to "varchar",
            "order_end_date" to "varchar",
            "enforceable_condition" to "varchar",
            "order_type" to "varchar",
            "order_type_description" to "varchar",
            "order_end_outcome" to "varchar",
            "responsible_org_details_phone_number" to "varchar",
            "responsible_org_details_email" to "varchar",
            "tag_at_source" to "varchar",
          ),
          arrayOf(
            arrayOf(
              "1000000",
              "987654321",
              "Pheobe",
              "Smith",
              "1970-01-01",
              "female",
              "test instructions",
              "address line 1",
              "address line 2",
              "",
              "postcode",
              "1253587",
              "1970-01-01",
              "1970-01-01",
              "condition",
              "community",
              "description",
              "outcome",
              "12345678",
              "a@b.c",
              "",
            ),
            arrayOf(
              "2000000",
              "987654321",
              "Bill",
              "Smith",
              "1970-01-01",
              "female",
              "test instructions",
              "address line 1",
              "address line 2",
              "",
              "postcode",
              "1253587",
              "1970-01-01",
              "1970-01-01",
              "condition",
              "community",
              "description",
              "outcome",
              "12345678",
              "a@b.c",
              "",
            ),
            arrayOf(
              "3000000",
              "987654321",
              "Claire",
              "Smith",
              "1970-01-01",
              "female",
              "test instructions",
              "address line 1",
              "address line 2",
              "",
              "postcode",
              "1253587",
              "1970-01-01",
              "1970-01-01",
              "condition",
              "community",
              "description",
              "outcome",
              "12345678",
              "a@b.c",
              "",
            ),
            arrayOf(
              "8000000",
              "987654321",
              "Daniel",
              "Smith",
              "1970-01-01",
              "female",
              "test instructions",
              "address line 1",
              "address line 2",
              "",
              "postcode",
              "1253587",
              "1970-01-01",
              "1970-01-01",
              "condition",
              "community",
              "description",
              "outcome",
              "12345678",
              "a@b.c",
              "",
            ),
            arrayOf(
              "30000",
              "987654321",
              "Emma",
              "Smith",
              "1970-01-01",
              "female",
              "test instructions",
              "address line 1",
              "address line 2",
              "",
              "postcode",
              "1253587",
              "1970-01-01",
              "1970-01-01",
              "condition",
              "community",
              "description",
              "outcome",
              "12345678",
              "a@b.c",
              "",
            ),
            arrayOf(
              "4000000",
              "987654321",
              "Fred",
              "Smith",
              "1970-01-01",
              "female",
              "test instructions",
              "address line 1",
              "address line 2",
              "",
              "postcode",
              "1253587",
              "1970-01-01",
              "1970-01-01",
              "condition",
              "community",
              "description",
              "outcome",
              "12345678",
              "a@b.c",
              "",
            ),
          ),
        ).toString(),
      )
    }

    @Test
    fun `should fail with 401 when no authorization header is provided`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring?id=HT-12345")
        .exchange()
        .expectStatus()
        .isUnauthorized
    }

    @Test
    fun `should fail with 403 when user has no required roles`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring?id=HT-12345")
        .headers(setAuthorisation(roles = listOf()))
        .exchange()
        .expectStatus()
        .isForbidden
    }

    @Test
    fun `should return 200 with valid configuration`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring?id=HT-12345")
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
        .uri("/orders/alcohol-monitoring")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .is5xxServerError
    }
  }

  @Nested
  @DisplayName("GET /orders/alcohol-monitoring/{legacySubjectId}")
  inner class GetDetails {

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
            "date_of_birth" to "varchar",
            "legacy_gender" to "varchar",
            "special_instructions" to "varchar",
            "phone_or_mobile_number" to "varchar",
            "primary_address_line_1" to "varchar",
            "primary_address_line_2" to "varchar",
            "primary_address_line_3" to "varchar",
            "primary_address_post_code" to "varchar",
            "order_start_date" to "varchar",
            "order_end_date" to "varchar",
            "enforceable_condition" to "varchar",
            "order_type" to "varchar",
            "order_type_description" to "varchar",
            "order_end_outcome" to "varchar",
            "responsible_org_details_phone_number" to "varchar",
            "responsible_org_details_email" to "varchar",
            "tag_at_source" to "varchar",
          ),
          arrayOf(
            arrayOf(
              "1253587",
              "987654321",
              "ELLEN",
              "RIPLEY",
              "1970-01-01",
              "female",
              "test instructions",
              "address line 1",
              "address line 2",
              "",
              "postcode",
              "1253587",
              "1970-01-01",
              "1970-01-01",
              "condition",
              "community",
              "description",
              "outcome",
              "12345678",
              "a@b.c",
              "",
            ),
          ),
        ).toString(),
      )
    }

    @Test
    fun `should return 401 unauthorized if no authorization header`() {
      noAuthHeaderRespondsWithUnauthorizedTest("/orders/alcohol-monitoring/234")
    }

    @Test
    fun `should return 403 forbidden if no role in authorization header`() {
      noRoleInAuthHeaderRespondsWithForbiddenTest("/orders/alcohol-monitoring/234")
    }

    @Test
    fun `should return 403 forbidden if wrong role in authorization header`() {
      wrongRolesRespondsWithForbiddenTest("/orders/alcohol-monitoring/234", listOf("ROLE_WRONG"))
    }

    @Test
    fun `should throw a Bad Request exception if the URL param format is invalid`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/2_4")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isBadRequest
    }

    @Test
    fun `should return OK with valid auth header and role for accessing restricted orders`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/234")
        .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_RESTRICTED__RO)))
        .exchange()
        .expectStatus()
        .isOk
    }

    @Test
    fun `should return OK with valid auth header and role for accessing general orders`() {
      webTestClient.get()
        .uri("/orders/alcohol-monitoring/234")
        .headers(setAuthorisation())
        .exchange()
        .expectStatus()
        .isOk
    }
  }
}
