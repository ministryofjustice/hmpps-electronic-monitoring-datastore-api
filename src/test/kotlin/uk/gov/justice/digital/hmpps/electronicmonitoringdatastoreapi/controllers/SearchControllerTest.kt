package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import net.minidev.json.JSONObject
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Order
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.SearchCriteria

class SearchControllerTest {

  private val sut: SearchController = SearchController()

  @Nested
  inner class GetSearchResult {

    @Test
    fun `Accepts caseId as parameter`() {
      val caseId = "obviously-fake-data"
      val expected = JSONObject(
        mapOf("data" to "You have successfully queried case obviously-fake-data"),
      )

      val result: JSONObject = sut.getCases(caseId)
      Assertions.assertThat(result).isEqualTo(expected)
    }
  }

  @Nested
  inner class SearchOrders {

    @Test
    fun `Returns a list of orders`() {
      // Arrange: Create search criteria
      val userToken = "fake-token"
      val searchCriteria = SearchCriteria(
        searchType = "name",
        legacySubjectId = "12345",
        firstName = "Amy",
        lastName = "Smith",
        alias = null,
        dobDay = "01",
        dobMonth = "01",
        dobYear = "1970",
      )

      val result: List<Order> = sut.searchOrders(userToken, searchCriteria)

      // Assert: Verify the result is a list of Order objects
      assertThat(result).isNotEmpty // Ensure the result is not empty
      assertThat(result).allMatch { it is Order } // Ensure all elements are of type Order
    }
  }
}
