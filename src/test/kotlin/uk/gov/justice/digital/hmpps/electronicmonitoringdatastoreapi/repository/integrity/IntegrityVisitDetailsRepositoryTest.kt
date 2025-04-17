package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityVisitDetailsDTO

class IntegrityVisitDetailsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: IntegrityVisitDetailsRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = IntegrityVisitDetailsRepository(emDatastoreClient)
  }

  @Test
  fun `VisitDetailsRepository can be instantiated`() {
    val sut = IntegrityVisitDetailsRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetVisitDetails {
    fun visitDetailsResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
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
    ).build()

    @Test
    fun `getVisitDetails passes correct query to getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(visitDetailsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      repository.getVisitDetails("123", AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getVisitDetails returns an AthenaVisitDetailsDTO`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(visitDetailsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getVisitDetails("123", AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `getVisitDetails returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(visitDetailsResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getVisitDetails("987", AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(2)

      Assertions.assertThat(result.first()).isInstanceOf(AthenaIntegrityVisitDetailsDTO::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
    }
  }
}
