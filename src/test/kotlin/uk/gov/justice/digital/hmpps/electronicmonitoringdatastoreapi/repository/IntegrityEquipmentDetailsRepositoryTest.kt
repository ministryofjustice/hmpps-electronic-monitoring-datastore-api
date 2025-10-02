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

class IntegrityEquipmentDetailsRepositoryTest {
  private lateinit var athenaClient: EmDatastoreClient
  private lateinit var repository: IntegrityEquipmentDetailsRepository

  @BeforeEach
  fun setup() {
    athenaClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = IntegrityEquipmentDetailsRepository(athenaClient)
  }

  @Nested
  inner class GetEquipmentDetails {
    fun mockResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "hmu_id",
        "hmu_equipment_category_description",
        "hmu_install_date",
        "hmu_install_time",
        "hmu_removed_date",
        "hmu_removed_time",
        "pid_id",
        "pid_equipment_category_description",
        "date_device_installed",
        "time_device_installed",
        "date_device_removed",
        "time_device_removed",
      ),
      rows = arrayOf(
        arrayOf(
          firstId,
          "123456789X",
          "TEST_HMU_CATEGORY",
          "2024-02-02",
          "02:02:02",
          "2024-03-03",
          "03:03:03",
          "123456789P",
          "TEST_PID_CATEGORY",
          "2024-02-02",
          "02:02:02",
          "2024-03-03",
          "03:03:03",
        ),
        arrayOf(
          "123456789",
          "987654321Y",
          "TEST_HMU_CATEGORY",
          "2024-02-02",
          "02:02:02",
          "2024-03-03",
          "03:03:03",
          "987654321P",
          "TEST_PID_CATEGORY",
          "2024-02-02",
          "02:02:02",
          "2024-03-03",
          "03:03:03",
        ),
      ),
    ).toResultSet()

    @Test
    fun `getEquipmentDetails passes correct query to getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet())

      repository.findByLegacySubjectId("123", false)

      Mockito.verify(athenaClient).getQueryResult(queryExecutionId, false)
    }

    @Test
    fun `getEquipmentDetails returns all the results from getQueryResult`() {
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
}
