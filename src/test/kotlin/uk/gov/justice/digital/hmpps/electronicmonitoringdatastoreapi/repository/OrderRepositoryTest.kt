package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaService

class OrderRepositoryTest {

  @Test
  fun `OrderRepository can be instantiated`() {
    val sut = OrderRepository(mock(AthenaService::class.java))
    Assertions.assertThat(sut).isNotNull()
  }
}

