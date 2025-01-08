package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult

class OrderRepositoryTest {

  @Test
  fun `OrderRepository can be instantiated`() {
    val sut = OrderRepository()
    Assertions.assertThat(sut).isNotNull()
  }

  @Test
  fun `GetFakeOrders() returns List of Orders`() {
    val result: List<OrderSearchResult> = OrderRepository.getFakeOrders()

    Assertions.assertThat(result).contains(
      OrderSearchResult(
        dataType = "am",
        legacySubjectId = 3000000,
        name = "Claire Smith",
        address = "First line of address",
        alias = null,
        dateOfBirth = "09-04-1962",
        orderStartDate = "05-08-2001",
        orderEndDate = "05-08-2002",
      ),
    )
  }

  // TODO: Add the tests for this
  @Nested
  inner class ParseOrders {

    @Test
    fun `Parses a ResultsSet as a list of Orders`() {
    }
  }

  // TODO: Add the tests for this
  @Nested
  inner class GetOrders
}
