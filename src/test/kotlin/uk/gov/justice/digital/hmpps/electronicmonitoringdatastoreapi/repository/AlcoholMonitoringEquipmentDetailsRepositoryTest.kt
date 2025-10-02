package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.queryBuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder
import java.util.UUID

@ActiveProfiles("test")
class AlcoholMonitoringEquipmentDetailsRepositoryTest {
  private lateinit var athenaClient: EmDatastoreClient
  private lateinit var repository: AlcoholMonitoringEquipmentDetailsRepository

  @BeforeEach
  fun setup() {
    athenaClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = AlcoholMonitoringEquipmentDetailsRepository(athenaClient)
  }

  @Nested
  @DisplayName("GetEquipmentDetails")
  inner class GetEquipmentDetails {
    private fun mockResultSet(firstId: String, secondId: String) = MockAthenaResultSetBuilder(
      columns = mapOf(
        "legacy_subject_id" to "varchar",
        "device_type" to "varchar",
        "device_serial_number" to "varchar",
        "device_address_type" to "varchar",
        "leg_fitting" to "varchar",
        "date_device_installed" to "varchar",
        "time_device_installed" to "varchar",
        "date_device_removed" to "varchar",
        "time_device_removed" to "varchar",
        "hmu_install_date" to "varchar",
        "hmu_install_time" to "varchar",
        "hmu_removed_date" to "varchar",
        "hmu_removed_time" to "varchar",
      ),
      rows = arrayOf(
        arrayOf(
          firstId,
          "tag",
          "111",
          "primary",
          "left",
          "2001-01-01",
          "01:10:10",
          "2002-02-02",
          "02:20:20",
          "",
          "",
          "",
          "",
        ),
        arrayOf(
          secondId,
          "hmu",
          "222",
          "primary",
          "left",
          "",
          "",
          "",
          "",
          "2003-03-03",
          "03:30:30",
          "2004-04-04",
          "04:40:40",
        ),
      ),
    ).toResultSet()

    @Test
    fun `passes correct query to getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet("123", "456"))

      repository.findByLegacySubjectId("123")

      Mockito.verify(athenaClient).getQueryResult(queryExecutionId, false)
    }

    @Test
    fun `returns all the results from getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet("000", "999"))

      val result = repository.findByLegacySubjectId("000")

      Assertions.assertThat(result.size).isEqualTo(2)
      Assertions.assertThat(result[0].legacySubjectId).isEqualTo("000")
      Assertions.assertThat(result[1].legacySubjectId).isEqualTo("999")
    }
  }
}
