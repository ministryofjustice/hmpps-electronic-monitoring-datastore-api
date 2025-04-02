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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmServiceDTO

class AmServicesRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var amServicesRepository: AmServicesRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    amServicesRepository = AmServicesRepository(emDatastoreClient)
  }

  @Test
  fun `AmServicesRepository can be instantiated`() {
    val sut = AmServicesRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetServices {
    private fun amServicesResultSet(firstId: String = "987") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "legacy_order_id",
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
          "456",
          "2001-01-01",
          "2002-02-02",
          "service address 2",
          "2003-03-03",
          "2004-04-04",
          "hmu-2",
          "device-2",
        ),
      ),
    ).build()

    @Test
    fun `getServices passes correct query to getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amServicesResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      amServicesRepository.getServices("123", AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getServices returns a list of AthenaAmServiceDTO`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amServicesResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = amServicesRepository.getServices("123", AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
      Assertions.assertThat(result).allSatisfy {
        Assertions.assertThat(it).isInstanceOf(AthenaAmServiceDTO::class.java)
      }
    }

    @Test
    fun `getServices returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amServicesResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = amServicesRepository.getServices("987", AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(2)

      Assertions.assertThat(result.first()).isInstanceOf(AthenaAmServiceDTO::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
    }
  }
}
