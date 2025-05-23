package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.mocks.MockAthenaResultSetBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmEquipmentDetailsDTO

class AmEquipmentDetailsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var amEquipmentDetailsRepository: AmEquipmentDetailsRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    amEquipmentDetailsRepository = AmEquipmentDetailsRepository(emDatastoreClient)
  }

  @Test
  fun `AmEquipmentDetailsRepository can be instantiated`() {
    val sut = AmEquipmentDetailsRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetEquipmentDetails {
    private fun amEquipmentDetailsResultSet(firstId: String = "123") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "device_type",
        "device_serial_number",
        "device_address_type",
        "leg_fitting",
        "date_device_installed",
        "time_device_installed",
        "date_device_removed",
        "time_device_removed",
        "hmu_install_date",
        "hmu_install_time",
        "hmu_removed_date",
        "hmu_removed_time",
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
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amEquipmentDetailsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      amEquipmentDetailsRepository.getEquipmentDetails("123", AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getEquipmentDetails returns a list of AthenaAmEquipmentDetailsDTO`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amEquipmentDetailsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = amEquipmentDetailsRepository.getEquipmentDetails("123", AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
      Assertions.assertThat(result).allSatisfy {
        Assertions.assertThat(it).isInstanceOf(AthenaAmEquipmentDetailsDTO::class.java)
      }
    }

    @Test
    fun `getEquipmentDetails returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amEquipmentDetailsResultSet("000"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = amEquipmentDetailsRepository.getEquipmentDetails("000", AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(2)

      Assertions.assertThat(result.first()).isInstanceOf(AthenaAmEquipmentDetailsDTO::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("000")
    }
  }
}
