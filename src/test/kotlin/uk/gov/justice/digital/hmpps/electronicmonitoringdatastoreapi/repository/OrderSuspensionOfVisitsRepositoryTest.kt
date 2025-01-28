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

class OrderSuspensionOfVisitsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: OrderSuspensionOfVisitsRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = OrderSuspensionOfVisitsRepository(emDatastoreClient)
  }

  @Test
  fun `OrderSuspensionOfVisitsRepository can be instantiated`() {
    val sut = OrderSuspensionOfVisitsRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetServicesList {
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
          "",
          "",
          "",
          "",
          "",
        ),
        arrayOf(
          "123456789",
          "",
          "",
          "",
          "",
          "",
        ),
      ),
    ).build()

    @Test
    fun `getSuspensionOfVisitsList passes correct query to getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(suspensionOfVisitsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      repository.getSuspensionOfVisitsList("123", AthenaRole.DEV)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getSuspensionOfVisitsList returns an AthenaSuspensionOfVisitsListDTO`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(suspensionOfVisitsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getSuspensionOfVisitsList("123", AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `getSuspensionOfVisitsList returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(suspensionOfVisitsResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getSuspensionOfVisitsList("987", AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(2)

      Assertions.assertThat(result.first()).isInstanceOf(AthenaSuspensionOfVisitsDTO::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo(987)
    }
  }
}