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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaResultListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaServicesDTO

class OrderServicesRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: OrderServicesRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = OrderServicesRepository(emDatastoreClient)
  }

  @Test
  fun `OrderServicesRepository can be instantiated`() {
    val sut = OrderServicesRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetServicesList {
    fun servicesResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
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
          "",
          "",
          "",
          "",
          "",
          "",
          "",
          "",
          "",
          "0",
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
          "",
          "",
          "",
          "",
          "",
          "",
          "",
          "",
          "",
          "",
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
      val resultSet = AthenaHelper.Companion.resultSetFromJson(servicesResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      repository.getServicesList("123", AthenaRole.DEV)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getServicesList returns an AthenaServicesListDTO`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(servicesResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getServicesList("123", AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(AthenaResultListDTO::class.java)
    }

    @Test
    fun `getServicesList returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(servicesResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getServicesList("987", AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.pageSize).isEqualTo(2)

      Assertions.assertThat(result.items.first()).isInstanceOf(AthenaServicesDTO::class.java)
      Assertions.assertThat(result.items.first().legacySubjectId).isEqualTo(987)
      Assertions.assertThat(result.items.first().serviceId).isEqualTo(333)
    }
  }
}
