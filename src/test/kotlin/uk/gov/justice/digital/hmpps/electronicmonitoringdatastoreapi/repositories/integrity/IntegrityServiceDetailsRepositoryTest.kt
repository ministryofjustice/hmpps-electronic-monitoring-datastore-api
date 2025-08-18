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

class IntegrityServiceDetailsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: IntegrityServiceDetailsRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = IntegrityServiceDetailsRepository(emDatastoreClient)
  }

  @Nested
  inner class GetServicesList {
    fun serviceDetailsResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "service_id",
        "service_address_1",
        "service_address_2",
        "service_address_3",
        "service_address_postcode",
        "service_start_date",
        "service_end_date",
        "curfew_start_date",
        "curfew_end_date",
        "curfew_start_time",
        "curfew_end_time",
        "monday",
        "tuesday",
        "wednesday",
        "thursday",
        "friday",
        "saturday",
        "sunday",
      ),
      rows = arrayOf(
        arrayOf(
          firstId,
          "333",
          "service_address_1",
          "service_address_2",
          "service_address_3",
          "service_address_postcode",
          "2002-02-02",
          "2004-04-04",
          "2002-02-02",
          "2004-04-04",
          "02:02:02",
          "04:04:04",
          "0",
          "0",
          "0",
          "0",
          "0",
          "0",
          "0",
        ),
        arrayOf(
          "123456789",
          "444",
          "service_address_1",
          "service_address_2",
          "service_address_3",
          "service_address_postcode",
          "2002-02-02",
          "2004-04-04",
          "2002-02-02",
          "2004-04-04",
          "02:02:02",
          "04:04:04",
          "0",
          "0",
          "0",
          "0",
          "0",
          "0",
          "0",
        ),
      ),
    ).build()

    @Test
    fun `getServicesList passes correct query to getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(serviceDetailsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      repository.getServiceDetails("123", false)

      Mockito.verify(emDatastoreClient).getQueryResult(any<SqlQueryBuilder>(), eq(false))
    }

    @Test
    fun `getServicesList returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(serviceDetailsResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      val result = repository.getServiceDetails("987", false)

      Assertions.assertThat(result.size).isEqualTo(2)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
      Assertions.assertThat(result.first().serviceId).isEqualTo(333)
    }
  }
}
