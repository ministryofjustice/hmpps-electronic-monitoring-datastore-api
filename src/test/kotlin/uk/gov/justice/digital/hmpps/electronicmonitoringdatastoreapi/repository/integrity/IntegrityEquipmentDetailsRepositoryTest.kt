package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.mocks.MockAthenaResultSetBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityEquipmentDetailsDTO

class IntegrityEquipmentDetailsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: IntegrityEquipmentDetailsRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = IntegrityEquipmentDetailsRepository(emDatastoreClient)
  }

  @Test
  fun `EquipmentDetailsRepository can be instantiated`() {
    val sut = IntegrityEquipmentDetailsRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetEquipmentDetails {
    fun equipmentDetailsResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
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
    ).build()

    @Test
    fun `getEquipmentDetails passes correct query to getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(equipmentDetailsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), eq(false))).thenReturn(resultSet)

      repository.getEquipmentDetails("123", false)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), eq(false))
    }

    @Test
    fun `getEquipmentDetails returns an AthenaSuspensionOfVisitsDTO`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(equipmentDetailsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), eq(false))).thenReturn(resultSet)

      val result = repository.getEquipmentDetails("123", false)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `getEquipmentDetails returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(equipmentDetailsResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), eq(false))).thenReturn(resultSet)

      val result = repository.getEquipmentDetails("987", false)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(2)

      Assertions.assertThat(result.first()).isInstanceOf(AthenaIntegrityEquipmentDetailsDTO::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
    }
  }
}
