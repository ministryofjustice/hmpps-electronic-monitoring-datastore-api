package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSuspensionOfVisitsDTO
import kotlin.String
import kotlin.collections.first

class SuspensionOfVisitsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: SuspensionOfVisitsRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = SuspensionOfVisitsRepository(emDatastoreClient)
  }

  @Test
  fun `OrderSuspensionOfVisitsRepository can be instantiated`() {
    val sut = SuspensionOfVisitsRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetSuspensionOfVisits {
    fun suspensionOfVisitsResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "suspension_of_visits",
        "suspension_of_visits_requested_date",
        "suspension_of_visits_start_date",
        "suspension_of_visits_start_time",
        "suspension_of_visits_end_date",
      ),
      rows = arrayOf(
        arrayOf(
          firstId,
          "Yes",
          "2024-02-02T02:02:02",
          "2024-02-02",
          "02:02:02",
          "2024-02-04T04:04:04",
        ),
        arrayOf(
          "123456789",
          "No",
          "2024-02-02T02:02:02",
          "2024-02-02",
          "02:02:02",
          "2024-02-04T04:04:04",
        ),
      ),
    ).build()

    fun suspensionOfVisitsNoStartResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "suspension_of_visits",
        "suspension_of_visits_requested_date",
        "suspension_of_visits_start_date",
        "suspension_of_visits_start_time",
        "suspension_of_visits_end_date",
      ),
      rows = arrayOf(
        arrayOf(
          firstId,
          "Yes",
          "2024-02-02T02:02:02",
          "",
          "",
          "2024-02-04T04:04:04",
        ),
      ),
    ).build()

    @Test
    fun `getSuspensionOfVisits passes correct query to getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(suspensionOfVisitsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      repository.getSuspensionOfVisits("123", AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getSuspensionOfVisits returns an AthenaSuspensionOfVisitsDTO`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(suspensionOfVisitsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getSuspensionOfVisits("123", AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `getSuspensionOfVisits returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(suspensionOfVisitsResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getSuspensionOfVisits("987", AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(2)

      Assertions.assertThat(result.first()).isInstanceOf(AthenaSuspensionOfVisitsDTO::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo(987)
    }

    @Test
    fun `getSuspensionOfVisits returns all the results from getQueryResult even if no start date and time is present`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(suspensionOfVisitsNoStartResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getSuspensionOfVisits("987", AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(AthenaSuspensionOfVisitsDTO::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo(987)
    }
  }
}
