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

class AmServiceDetailsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var amServiceDetailsRepository: AmServiceDetailsRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    amServiceDetailsRepository = AmServiceDetailsRepository(emDatastoreClient)
  }

  @Nested
  inner class GetServices {
    private fun amServicesResultSet(firstId: String = "987") = MockAthenaResultSetBuilder(
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
    ).build()

    @Test
    fun `getServices passes correct query to getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amServicesResultSet("123"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      amServiceDetailsRepository.findByLegacySubjectId("123")

      Mockito.verify(emDatastoreClient).getQueryResult(any<SqlQueryBuilder>(), eq(false))
    }

    @Test
    fun `getServices returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amServicesResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      val result = amServiceDetailsRepository.findByLegacySubjectId("987")

      Assertions.assertThat(result.size).isEqualTo(2)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
    }
  }
}
