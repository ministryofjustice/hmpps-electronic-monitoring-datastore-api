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

class AlcoholMonitoringServiceDetailsRepositoryTest {
  private lateinit var athenaClient: EmDatastoreClient
  private lateinit var repository: AlcoholMonitoringServiceDetailsRepository

  @BeforeEach
  fun setup() {
    athenaClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = AlcoholMonitoringServiceDetailsRepository(athenaClient)
  }

  @Nested
  inner class GetServices {
    private fun mockResultSet(firstId: String = "987") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "service_start_date",
        "service_end_date",
        "service_address",
        "equipment_start_date",
        "equipment_end_date",
        "hmu_serial_number",
        "device_serial_number",
      ),
      rows = arrayOf(
        arrayOf(
          firstId,
          "2001-01-01",
          "2002-02-02",
          "service address 1",
          "2003-03-03",
          "2004-04-04",
          "hmu-1",
          "device-1",
        ),
        arrayOf(
          "123",
          "2001-01-01",
          "2002-02-02",
          "service address 2",
          "2003-03-03",
          "2004-04-04",
          "hmu-2",
          "device-2",
        ),
      ),
    ).toResultSet()

    @Test
    fun `getServices passes correct query to getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet("123"))

      repository.findByLegacySubjectId("123")

      Mockito.verify(athenaClient).getQueryResult(queryExecutionId, false)
    }

    @Test
    fun `getServices returns all the results from getQueryResult`() {
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
