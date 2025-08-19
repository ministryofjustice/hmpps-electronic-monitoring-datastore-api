package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.integrity

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
import kotlin.String
import kotlin.collections.first

class IntegritySuspensionOfVisitsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: IntegritySuspensionOfVisitsRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = IntegritySuspensionOfVisitsRepository(emDatastoreClient)
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

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      repository.findByLegacySubjectIdAndRestricted("123", false)

      Mockito.verify(emDatastoreClient).getQueryResult(any<SqlQueryBuilder>(), eq(false))
    }

    @Test
    fun `getSuspensionOfVisits returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(suspensionOfVisitsResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      val result = repository.findByLegacySubjectIdAndRestricted("987", false)

      Assertions.assertThat(result.size).isEqualTo(2)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
    }

    @Test
    fun `getSuspensionOfVisits returns all the results from getQueryResult even if no start date and time is present`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(suspensionOfVisitsNoStartResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      val result = repository.findByLegacySubjectIdAndRestricted("987", false)

      Assertions.assertThat(result.size).isEqualTo(1)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
    }
  }
}
