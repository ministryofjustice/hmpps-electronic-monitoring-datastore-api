package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import net.minidev.json.JSONObject
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SearchControllerTest {

  @Nested
  inner class GetSearchResult {

    private val sut: SearchController = SearchController()

    @Test
    fun `Accepts caseId as parameter`() {
      val caseId = "obviously-fake-data"
      val expected = JSONObject(
        mapOf("data" to "You have successfully queried case obviously-fake-data")
      )

      val result: JSONObject = sut.getCases(caseId)
      Assertions.assertThat(result).isEqualTo(expected)
    }
  }
}
