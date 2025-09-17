package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.integrity

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder
import java.time.LocalDate

class IntegrityOrderDetailsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: IntegrityOrderDetailsRepository

  fun orderDetailsResultSet(
    subjectId: String,
    orderId: String,
    offenceRisk: Boolean,
  ) = MockAthenaResultSetBuilder(
    columns = mapOf(
      "legacy_subject_id" to "varchar",
      "legacy_order_id" to "varchar",
      "offence_risk" to "boolean",
    ),
    rows = arrayOf(
      arrayOf(subjectId, orderId, offenceRisk.toString()),
      arrayOf("5678", "This row should never be returned", "false"),
    ),
  ).build()

  @BeforeEach
  fun setup() {
    emDatastoreClient = mock<EmDatastoreClient>()
    repository = IntegrityOrderDetailsRepository(emDatastoreClient)
  }

  @Nested
  inner class GetOrderDetails {
    @Test
    fun `getOrderDetails calls getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(orderDetailsResultSet("123564", "768324", true))

      whenever(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      repository.findByLegacySubjectIdAndRestricted("123", false)

      verify(emDatastoreClient).getQueryResult(any<SqlQueryBuilder>(), eq(false))
    }

    @Test
    fun `getOrderDetails returns the first result from getQueryResult`() {
      val legacySubjectId = "expectedId"
      val resultSet = AthenaHelper.resultSetFromJson(orderDetailsResultSet(legacySubjectId, "123564", false))

      whenever(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      val result = repository.findByLegacySubjectIdAndRestricted(legacySubjectId, false)

      assertThat(result.legacySubjectId).isEqualTo(legacySubjectId)
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
