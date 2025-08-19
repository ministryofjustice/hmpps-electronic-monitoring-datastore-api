package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder

class AmEquipmentDetailsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var amEquipmentDetailsRepository: AmEquipmentDetailsRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    amEquipmentDetailsRepository = AmEquipmentDetailsRepository(emDatastoreClient)
  }

  @Nested
  inner class GetEquipmentDetails {
    private fun amEquipmentDetailsResultSet(firstId: String) = MockAthenaResultSetBuilder(
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
          "456",
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
    ).build()

    @Test
    fun `getEquipmentDetails passes correct query to getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(amEquipmentDetailsResultSet("123"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      amEquipmentDetailsRepository.findByLegacySubjectId("123")

      Mockito.verify(emDatastoreClient).getQueryResult(any<SqlQueryBuilder>(), eq(false))
    }

    @Test
    fun `getEquipmentDetails returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(amEquipmentDetailsResultSet("000"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      val result = amEquipmentDetailsRepository.findByLegacySubjectId("000")

      Assertions.assertThat(result.size).isEqualTo(2)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("000")
    }
  }
}
