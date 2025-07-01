package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder
import java.time.LocalDate

class SearchRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: SearchRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = SearchRepository(emDatastoreClient)
  }

  @Nested
  inner class SearchOrders {
    @Test
    fun `searchOrders calls getQueryExecutionId with the correct sql query`() {
      val argumentCaptor = argumentCaptor<SqlQueryBuilder>()
      val searchCriteria = OrderSearchCriteria(
        searchType = "integrity",
        legacySubjectId = "123456",
        firstName = "Test First Name",
        lastName = "Test Last Name",
        alias = "Test Alias",
        dateOfBirth = LocalDate.parse("2001-01-01"),
      )

      repository.searchOrders(searchCriteria, false)

      Mockito.verify(emDatastoreClient).getQueryExecutionId(argumentCaptor.capture(), eq(false))
      Assertions.assertThat(argumentCaptor.firstValue.values).isEqualTo(
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
      val searchCriteria = OrderSearchCriteria(
        searchType = "integrity",
      )

      Mockito.`when`(emDatastoreClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false))).thenReturn(expected)

      val result = repository.searchOrders(searchCriteria, false)
      Assertions.assertThat(result).isEqualTo(expected)
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

      Mockito.`when`(emDatastoreClient.getQueryResult(eq(queryExecutionId), eq(false))).thenReturn(validResponse)

      repository.getSearchResults(queryExecutionId, false)

      Mockito.verify(emDatastoreClient).getQueryResult(argumentCaptor.capture(), eq(false))
      Assertions.assertThat(argumentCaptor.firstValue).isEqualTo(queryExecutionId)
    }

    @Test
    fun `searchOrders returns the query execution id from Athena`() {
      val queryExecutionId = "execution-id-test"

      Mockito.`when`(emDatastoreClient.getQueryResult(eq(queryExecutionId), eq(false))).thenReturn(validResponse)

      val result = repository.getSearchResults(queryExecutionId, false)

      Assertions.assertThat(result).hasSize(2)
      Assertions.assertThat(result[0].legacySubjectId).isEqualTo("123564")
      Assertions.assertThat(result[1].legacySubjectId).isEqualTo("5678")
    }
  }
}
