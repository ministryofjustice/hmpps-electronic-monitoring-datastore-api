package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.queryBuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.queryBuilders.SqlQueryBuilderBase
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder
import java.time.LocalDate
import java.util.UUID

class IntegrityOrderDetailsRepositoryTest {
  private lateinit var athenaClient: EmDatastoreClient
  private lateinit var repository: IntegrityOrderDetailsRepository

  fun mockResultSet(
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
  ).toResultSet()

  @BeforeEach
  fun setup() {
    athenaClient = mock<EmDatastoreClient>()
    repository = IntegrityOrderDetailsRepository(athenaClient)
  }

  @Nested
  inner class GetOrderDetails {
    @Test
    fun `getOrderDetails calls getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet("123564", "768324", true))

      repository.getByLegacySubjectId("123", false)

      verify(athenaClient).getQueryResult(queryExecutionId, false)
    }

    @Test
    fun `getOrderDetails returns the first result from getQueryResult`() {
      val legacySubjectId = "expectedId"

      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet(legacySubjectId, "123564", false))

      val result = repository.getByLegacySubjectId(legacySubjectId, false)

      assertThat(result.legacySubjectId).isEqualTo(legacySubjectId)
    }
  }

  @Nested
  inner class SearchOrders {
    @Test
    fun `searchOrders calls getQueryExecutionId with the correct sql query`() {
      val argumentCaptor = argumentCaptor<SqlQueryBuilderBase<Any>>()
      val searchCriteria = OrderSearchCriteria(
        legacySubjectId = "123456",
        firstName = "Test First Name",
        lastName = "Test Last Name",
        alias = "Test Alias",
        dateOfBirth = LocalDate.parse("2001-01-01"),
      )

      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)

      repository.searchOrders(searchCriteria, false)

      verify(athenaClient).getQueryExecutionId(argumentCaptor.capture(), eq(false))
      assertThat(argumentCaptor.firstValue.values).isEqualTo(
        listOf(
          "UPPER('%Test Alias%')",
          "UPPER('%2001-01-01%')",
          "UPPER('%Test First Name%')",
          "UPPER('%Test Last Name%')",
          "UPPER('%123456%')",
        ),
      )
    }

    @Test
    fun `searchOrders returns the query execution id from Athena`() {
      val expected = "query-id"
      val searchCriteria = OrderSearchCriteria()

      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(expected)

      val result = repository.searchOrders(searchCriteria, false)
      assertThat(result).isEqualTo(expected)
    }
  }

  @Nested
  inner class GetSearchResults {
    val validResponse = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "first_name",
        "last_name",
      ),
      rows = arrayOf(
        arrayOf("123564", "John", "Doh"),
        arrayOf("5678", "Martin", "Martian"),
      ),
    ).toResultSet()

    @Test
    fun `getSearchResults calls getQueryExecutionId with the correct sql query`() {
      val argumentCaptor = argumentCaptor<String>()
      val queryExecutionId = "test-id"

      whenever(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(validResponse)

      repository.getSearchResults(queryExecutionId, false)

      verify(athenaClient).getQueryResult(argumentCaptor.capture(), eq(false))
      assertThat(argumentCaptor.firstValue).isEqualTo(queryExecutionId)
    }

    @Test
    fun `searchOrders returns the query execution id from Athena`() {
      val queryExecutionId = "execution-id-test"

      whenever(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(validResponse)

      val result = repository.getSearchResults(queryExecutionId, false)

      assertThat(result).hasSize(2)
      assertThat(result[0].legacySubjectId).isEqualTo("123564")
      assertThat(result[1].legacySubjectId).isEqualTo("5678")
    }
  }
}
