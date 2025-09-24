package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder
import java.util.UUID

class AlcoholMonitoringVisitDetailsRepositoryTest {
  private lateinit var athenaClient: EmDatastoreClient
  private lateinit var repository: AlcoholMonitoringVisitDetailsRepository

  @BeforeEach
  fun setup() {
    athenaClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = AlcoholMonitoringVisitDetailsRepository(athenaClient)
  }

  @Nested
  inner class GetVisitDetails {
    private fun mockResultSet(firstId: String = "987") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "visit_id",
        "visit_type",
        "visit_attempt",
        "date_visit_raised",
        "visit_address",
        "visit_notes",
        "visit_outcome",
        "actual_work_start_date",
        "actual_work_start_time",
        "actual_work_end_date",
        "actual_work_end_time",
        "visit_rejection_reason",
        "visit_rejection_description",
        "visit_cancel_reason",
        "visit_cancel_description",
      ),
      rows = arrayOf(
        arrayOf(
          firstId,
          firstId,
          "visit type",
          "attempt 1",
          "2001-01-01",
          "visit address",
          "visit notes",
          "visit outcome",
          "2002-02-02",
          "02:20:20",
          "2003-03-03",
          "03:30:30",
          "rejection reason",
          "rejection description",
          "cancel reason",
          "cancel description",
        ),
        arrayOf(
          "987",
          "123",
          "community",
          "attempt 4",
          "2001-01-01",
          "1 primary street, A23 45F",
          "some notes",
          "some outcome",
          "2002-02-02",
          "02:20:20",
          "2003-03-03",
          "03:30:30",
          "rejection reason",
          "rejection description",
          "cancel reason",
          "cancel description",
        ),
      ),
    ).toResultSet()

    @Test
    fun `getVisitDetails passes correct query to getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet("123"))

      repository.findByLegacySubjectId("123")

      Mockito.verify(athenaClient).getQueryResult(queryExecutionId, false)
    }

    @Test
    fun `getVisitDetails returns all the results from getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet("987"))

      val result = repository.findByLegacySubjectId("987")

      Assertions.assertThat(result.size).isEqualTo(2)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
    }
  }
}
