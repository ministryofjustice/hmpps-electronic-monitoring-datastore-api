package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.datastore.DatastoreProperties
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder
import java.util.UUID

class IntegrityServiceDetailsRepositoryTest {
  private lateinit var athenaClient: EmDatastoreClient
  private lateinit var repository: IntegrityServiceDetailsRepository

  @BeforeEach
  fun setup() {
    athenaClient = Mockito.mock(EmDatastoreClient::class.java)
    Mockito.`when`(athenaClient.properties)
      .thenReturn(DatastoreProperties(database = "fake-database", outputBucketArn = "fake-arn"))

    repository = IntegrityServiceDetailsRepository(athenaClient)
  }

  @Nested
  inner class GetServicesList {
    fun mockResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
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
    ).toResultSet()

    @Test
    fun `getServicesList passes correct query to getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<AthenaQuery>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet())

      repository.findByLegacySubjectId("123", false)

      Mockito.verify(athenaClient).getQueryResult(queryExecutionId, false)
    }

    @Test
    fun `getServicesList returns all the results from getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<AthenaQuery>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet("987"))

      val result = repository.findByLegacySubjectId("987", false)

      Assertions.assertThat(result.size).isEqualTo(2)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
      Assertions.assertThat(result.first().serviceId).isEqualTo(333)
    }
  }
}
