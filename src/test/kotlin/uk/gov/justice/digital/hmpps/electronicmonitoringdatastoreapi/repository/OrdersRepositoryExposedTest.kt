package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class OrdersRepositoryExposedTest {

  val sut: OrdersRepositoryExposed = OrdersRepositoryExposed()

  @Test
  fun `Instantiates`(){
    Assertions.assertThat(sut).isNotNull
  }

  @Test
  fun `returns a string`(){
    val result: String = sut.generate()
    Assertions.assertThat(result).isNotNull()
  }
}