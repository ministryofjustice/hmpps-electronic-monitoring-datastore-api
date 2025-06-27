package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmVisitDetailsDTO

class AmVisitDetailsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var amVisitDetailsRepository: AmVisitDetailsRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    amVisitDetailsRepository = AmVisitDetailsRepository(emDatastoreClient)
  }

  @Test
  fun `AmVisitDetailsRepository can be instantiated`() {
    val sut = AmVisitDetailsRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetVisitDetails {
    private fun amVisitDetailsResultSet(firstId: String = "987") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "visit_id",
        "visit_type",
        "visit_attempt",
        "date_visit_raised",
        "visit_address",
        "visit_notes",
        "visit_outcome",
        "actual_work_start_date",
        "actual_work_start_time",
        "actual_work_end_date",
        "actual_work_end_time",
        "visit_rejection_reason",
        "visit_rejection_description",
        "visit_cancel_reason",
        "visit_cancel_description",
      ),
      rows = arrayOf(
        arrayOf(
          firstId,
          firstId,
          "visit type",
          "attempt 1",
          "2001-01-01",
          "visit address",
          "visit notes",
          "visit outcome",
          "2002-02-02",
          "02:20:20",
          "2003-03-03",
          "03:30:30",
          "rejection reason",
          "rejection description",
          "cancel reason",
          "cancel description",
        ),
        arrayOf(
          "987",
          "123",
          "community",
          "attempt 4",
          "2001-01-01",
          "1 primary street, A23 45F",
          "some notes",
          "some outcome",
          "2002-02-02",
          "02:20:20",
          "2003-03-03",
          "03:30:30",
          "rejection reason",
          "rejection description",
          "cancel reason",
          "cancel description",
        ),
      ),
    ).build()

    @Test
    fun `getVisitDetails passes correct query to getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amVisitDetailsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), eq(false))).thenReturn(resultSet)

      amVisitDetailsRepository.getVisitDetails("123")

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), eq(false))
    }

    @Test
    fun `getVisitDetails returns a list of AthenaAmVisitDetailsDTO`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amVisitDetailsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), eq(false))).thenReturn(resultSet)

      val result = amVisitDetailsRepository.getVisitDetails("123")

      Assertions.assertThat(result).isInstanceOf(List::class.java)
      Assertions.assertThat(result).allSatisfy {
        Assertions.assertThat(it).isInstanceOf(AthenaAmVisitDetailsDTO::class.java)
      }
    }

    @Test
    fun `getVisitDetails returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amVisitDetailsResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), eq(false))).thenReturn(resultSet)

      val result = amVisitDetailsRepository.getVisitDetails("987")

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(2)

      Assertions.assertThat(result.first()).isInstanceOf(AthenaAmVisitDetailsDTO::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
    }
  }
}
