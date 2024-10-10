package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SearchControllerTest {

  @Nested
  inner class GetSearchResult {

    private val sut: SearchController = SearchController()

    @Test
    fun `Accepts caseId as parameter`() {
      val caseId = "12345"
      val result: String = sut.getCases(caseId)
      val expected = "Successful search with ID 12345"
      Assertions.assertThat(result).isEqualTo(expected)
    }
  }
}
