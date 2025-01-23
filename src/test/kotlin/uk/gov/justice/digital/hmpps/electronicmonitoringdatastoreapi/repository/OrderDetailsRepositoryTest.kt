package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient

class OrderDetailsRepositoryTest {

  @Test
  fun `OrderDetailsRepository can be instantiated`() {
    val sut = OrderRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetOrderDetails {
//    TODO: THIS TEST FIRST!
    @Test
    fun `getOrderDetails calls getQueryResult`() {
//      val resultSet = AthenaHelper.resultSetFromJson(keyOrderInformationResultSet())
//
//      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)
//
//      repository.getKeyOrderInformation("123", AthenaRole.DEV)
//
//      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }
  }
}
