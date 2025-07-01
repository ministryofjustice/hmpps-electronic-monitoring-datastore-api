package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder
import java.time.LocalDate

class AmOrderDetailsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: AmOrderDetailsRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = mock<EmDatastoreClient>()
    repository = AmOrderDetailsRepository(emDatastoreClient)
  }

  @Nested
  inner class GetAmOrderDetails {
    fun amOrderDetailsData(subjectId: String, orderId: String, firstName: String, lastName: String): Array<String> = arrayOf(
      subjectId,
      firstName,
      lastName,
      "alias",
      "1970-01-01",
      "sex",
      "special instructions",
      "09876543210",
      "address line 1",
      "",
      "address line 3",
      "postcode",
      orderId,
      "1970-01-01",
      "1970-01-01",
      "enforceable condition",
      "order type",
      "order type description",
      "order end outcome",
      "responsible organisation phone number",
      "responsible organisation email",
      "tag at source",
    )

    fun amOrderDetailsResultSet(firstSubjectId: String? = "7853521", firstOrderId: String? = "1253587", firstFirstName: String? = "ELLEN", firstLastName: String? = "RIPLEY"): String = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "first_name",
        "last_name",
        "alias",
        "date_of_birth",
        "legacy_gender",
        "special_instructions",
        "phone_or_mobile_number",
        "primary_address_line_1",
        "primary_address_line_2",
        "primary_address_line_3",
        "primary_address_post_code",
        "legacy_order_id",
        "order_start_date",
        "order_end_date",
        "enforceable_condition",
        "order_type",
        "order_type_description",
        "order_end_outcome",
        "responsible_org_details_phone_number",
        "responsible_org_details_email",
        "tag_at_source",
      ),
      rows = arrayOf(
        amOrderDetailsData(firstOrderId!!, firstSubjectId!!, firstFirstName!!, firstLastName!!),
        amOrderDetailsData("1034415", "1032792", "JOHN", "BROWNLIE"),
      ),
    ).build()

    @Test
    fun `getOrderDetails calls getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(amOrderDetailsResultSet())

      whenever(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      repository.getOrderDetails("123")

      verify(emDatastoreClient).getQueryResult(any<SqlQueryBuilder>(), eq(false))
    }

    @Test
    fun `getOrderDetails returns the first result from getQueryResult`() {
      val legacySubjectId = "subjectId777"
      val legacyOrderId = "orderId777"
      val firstName = "TEST FIRST NAME"
      val lastName = "TEST LAST NAME"

      val resultSet = AthenaHelper.resultSetFromJson(amOrderDetailsResultSet(legacyOrderId, legacySubjectId, firstName, lastName))

      whenever(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      val result = repository.getOrderDetails(legacySubjectId)

      assertThat(result.legacyOrderId).isEqualTo(legacyOrderId)
      assertThat(result.legacySubjectId).isEqualTo(legacySubjectId)
      assertThat(result.firstName).isEqualTo(firstName)
      assertThat(result.lastName).isEqualTo(lastName)
    }
  }

  @Nested
  inner class SearchOrders {
    @Test
    fun `searchOrders calls getQueryExecutionId with the correct sql query`() {
      val argumentCaptor = argumentCaptor<SqlQueryBuilder>()
      val searchCriteria = OrderSearchCriteria(
        legacySubjectId = "123456",
        firstName = "Test First Name",
        lastName = "Test Last Name",
        alias = "Test Alias",
        dateOfBirth = LocalDate.parse("2001-01-01"),
      )

      repository.searchOrders(searchCriteria, false)

      verify(emDatastoreClient).getQueryExecutionId(argumentCaptor.capture(), eq(false))
      assertThat(argumentCaptor.firstValue.values).isEqualTo(
        listOf(
          "UPPER('123456')",
          "UPPER('%Test First Name%')",
          "UPPER('%Test Last Name%')",
          "UPPER('%Test Alias%')",
          "DATE '2001-01-01'",
        ),
      )
    }

    @Test
    fun `searchOrders returns the query execution id from Athena`() {
      val expected = "query-id"
      val searchCriteria = OrderSearchCriteria()

      whenever(emDatastoreClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false))).thenReturn(expected)

      val result = repository.searchOrders(searchCriteria, false)
      assertThat(result).isEqualTo(expected)
    }
  }

  @Nested
  inner class GetSearchResults {
    val validResponse = AthenaHelper.resultSetFromJson(
      MockAthenaResultSetBuilder(
        columns = arrayOf(
          "legacy_subject_id",
          "first_name",
          "last_name",
        ),
        rows = arrayOf(
          arrayOf("123564", "John", "Doh"),
          arrayOf("5678", "Martin", "Martian"),
        ),
      ).build(),
    )

    @Test
    fun `getSearchResults calls getQueryExecutionId with the correct sql query`() {
      val argumentCaptor = argumentCaptor<String>()
      val queryExecutionId = "test-id"

      whenever(emDatastoreClient.getQueryResult(eq(queryExecutionId), eq(false))).thenReturn(validResponse)

      repository.getSearchResults(queryExecutionId, false)

      verify(emDatastoreClient).getQueryResult(argumentCaptor.capture(), eq(false))
      assertThat(argumentCaptor.firstValue).isEqualTo(queryExecutionId)
    }

    @Test
    fun `searchOrders returns the query execution id from Athena`() {
      val queryExecutionId = "execution-id-test"

      whenever(emDatastoreClient.getQueryResult(eq(queryExecutionId), eq(false))).thenReturn(validResponse)

      val result = repository.getSearchResults(queryExecutionId, false)

      assertThat(result).hasSize(2)
      assertThat(result[0].legacySubjectId).isEqualTo("123564")
      assertThat(result[1].legacySubjectId).isEqualTo("5678")
    }
  }
}
