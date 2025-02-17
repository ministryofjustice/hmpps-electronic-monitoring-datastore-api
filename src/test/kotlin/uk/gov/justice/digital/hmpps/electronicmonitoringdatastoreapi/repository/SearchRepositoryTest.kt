package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient

class SearchRepositoryTest {

  @Test
  fun `OrderRepository can be instantiated`() {
    val sut = SearchRepository(mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }
}
