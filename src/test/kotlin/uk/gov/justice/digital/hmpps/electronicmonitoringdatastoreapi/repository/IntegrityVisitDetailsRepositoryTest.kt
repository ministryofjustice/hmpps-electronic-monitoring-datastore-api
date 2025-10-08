package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.datastore.DatastoreProperties
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder
import java.util.UUID

class IntegrityVisitDetailsRepositoryTest {
  private lateinit var athenaClient: EmDatastoreClient
  private lateinit var repository: IntegrityVisitDetailsRepository

  @BeforeEach
  fun setup() {
    athenaClient = Mockito.mock(EmDatastoreClient::class.java)
    Mockito.`when`(athenaClient.properties)
      .thenReturn(DatastoreProperties(database = "fake-database", outputBucketArn = "fake-arn"))

    repository = IntegrityVisitDetailsRepository(athenaClient)
  }

  @Nested
  inner class GetVisitDetails {
    fun mockResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "address_1",
        "address_2",
        "address_3",
        "postcode",
        "actual_work_start_date",
        "actual_work_start_time",
        "actual_work_end_date",
        "actual_work_end_time",
        "visit_notes",
        "visit_type",
        "visit_outcome",
      ),
      rows = arrayOf(
        arrayOf(
          firstId,
          "address_1",
          "address_2",
          "address_3",
          "postcode",
          "2020-20-20",
          "02:02:02",
          "3030-30-30",
          "03:03:03",
          "visit_notes",
          "TEST_VISIT_TYPE",
          "visit_outcome",
        ),
        arrayOf(
          "123456789",
          "address_1",
          "address_2",
          "address_3",
          "postcode",
          "2020-20-20",
          "02:02:02",
          "3030-30-30",
          "03:03:03",
          "visit_notes",
          "TEST_VISIT_TYPE",
          "visit_outcome",
        ),
      ),
    ).toResultSet()

    @Test
    fun `getVisitDetails passes correct query to getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<AthenaQuery>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet())

      repository.findByLegacySubjectId("123", false)

      Mockito.verify(athenaClient).getQueryResult(queryExecutionId, false)
    }

    @Test
    fun `getVisitDetails returns all the results from getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<AthenaQuery>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet("987"))

      val result = repository.findByLegacySubjectId("987", false)

      Assertions.assertThat(result.size).isEqualTo(2)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
    }
  }
}
