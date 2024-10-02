package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SearchControllerTest {

  @Nested
  inner class GetSearchResult {

    private val sut: SearchController = SearchController()

    @Test
    fun `returns response`() {
      val result: String = sut.getCases()
      val expected = "true"
      Assertions.assertThat(result).isEqualTo(expected)
    }
  }
}
