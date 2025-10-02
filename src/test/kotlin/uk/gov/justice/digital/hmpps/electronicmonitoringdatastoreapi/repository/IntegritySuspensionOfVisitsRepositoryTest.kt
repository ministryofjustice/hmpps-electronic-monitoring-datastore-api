package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.queryBuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder
import java.util.UUID
import kotlin.String
import kotlin.collections.first

class IntegritySuspensionOfVisitsRepositoryTest {
  private lateinit var athenaClient: EmDatastoreClient
  private lateinit var repository: IntegritySuspensionOfVisitsRepository

  @BeforeEach
  fun setup() {
    athenaClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = IntegritySuspensionOfVisitsRepository(athenaClient)
  }

  @Nested
  inner class GetSuspensionOfVisits {

    @Nested
    inner class WhenWithStartDateTime {
      fun mockResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
        columns = arrayOf(
          "legacy_subject_id",
          "suspension_of_visits",
          "suspension_of_visits_requested_date",
          "suspension_of_visits_start_date",
          "suspension_of_visits_start_time",
          "suspension_of_visits_end_date",
        ),
        rows = arrayOf(
          arrayOf(
            firstId,
            "Yes",
            "2024-02-02T02:02:02",
            "2024-02-02",
            "02:02:02",
            "2024-02-04T04:04:04",
          ),
          arrayOf(
            "123456789",
            "No",
            "2024-02-02T02:02:02",
            "2024-02-02",
            "02:02:02",
            "2024-02-04T04:04:04",
          ),
        ),
      ).toResultSet()

      @Test
      fun `passes correct query to getQueryResult`() {
        val queryExecutionId = UUID.randomUUID().toString()
        Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
          .thenReturn(queryExecutionId)
        Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
          .thenReturn(mockResultSet())

        repository.findByLegacySubjectId("123", false)

        Mockito.verify(athenaClient).getQueryResult(queryExecutionId, false)
      }

      @Test
      fun `returns all the results from getQueryResult`() {
        val queryExecutionId = UUID.randomUUID().toString()
        Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
          .thenReturn(queryExecutionId)
        Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
          .thenReturn(mockResultSet("987"))

        val result = repository.findByLegacySubjectId("987", false)

        Assertions.assertThat(result.size).isEqualTo(2)
        Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
      }
    }

    @Nested
    inner class WhenWithoutStartDateTime {
      fun mockResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
        columns = arrayOf(
          "legacy_subject_id",
          "suspension_of_visits",
          "suspension_of_visits_requested_date",
          "suspension_of_visits_start_date",
          "suspension_of_visits_start_time",
          "suspension_of_visits_end_date",
        ),
        rows = arrayOf(
          arrayOf(
            firstId,
            "Yes",
            "2024-02-02T02:02:02",
            "",
            "",
            "2024-02-04T04:04:04",
          ),
        ),
      ).toResultSet()

      @Test
      fun `returns all the results from getQueryResult even if no start date and time is present`() {
        val queryExecutionId = UUID.randomUUID().toString()
        Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
          .thenReturn(queryExecutionId)
        Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
          .thenReturn(mockResultSet("456"))

        val result = repository.findByLegacySubjectId("456", false)

        Assertions.assertThat(result.size).isEqualTo(1)
        Assertions.assertThat(result.first().legacySubjectId).isEqualTo("456")
      }
    }
  }
}
