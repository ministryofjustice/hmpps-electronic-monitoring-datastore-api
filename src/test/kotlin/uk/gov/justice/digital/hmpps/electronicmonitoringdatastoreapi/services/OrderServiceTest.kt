package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService

class OrderServiceTest {

  fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")

  @Nested
  inner class ParseSearchCriteria {

    val baseQuery: String = """
        SELECT 
          legacy_subject_id
          , full_name
          , primary_address_line_1
          , primary_address_line_2
          , primary_address_line_3
          , primary_address_post_code
          , order_start_date
          , order_end_date 
        FROM 
          test_database.order_details
        WHERE 
    """.trimIndent()

    @Test
    fun `returns valid SQL if only legacySubjectId is populated`() {
      val criteria = OrderSearchCriteria(
        legacySubjectId = "111222333",
      )

      val expectedSQL = replaceWhitespace(
        baseQuery + """
            legacy_subject_id = 111222333
        """.trimIndent(),
      )

      val result: AthenaQuery = OrderService.searchKeyOrderInformationQuery(criteria)

      Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    }

    @Test
    fun `returns valid SQL if only firstName is populated`() {
      val criteria = OrderSearchCriteria(
        firstName = "Steeevooooo",
      )

      val expectedSQL = replaceWhitespace(
        baseQuery + """
            upper(first_name) = upper('Steeevooooo')
        """.trimIndent(),
      )

      val result: AthenaQuery = OrderService.searchKeyOrderInformationQuery(criteria)

      Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    }

    @Test
    fun `returns valid SQL if only LastName is populated`() {
      val criteria = OrderSearchCriteria(
        lastName = "Jobs",
      )

      val expectedSQL = replaceWhitespace(
        baseQuery + """
            upper(last_name) = upper('Jobs')
        """.trimIndent(),
      )

      val result: AthenaQuery = OrderService.searchKeyOrderInformationQuery(criteria)

      Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    }

    @Test
    fun `returns valid SQL if only alias is populated`() {
      val criteria = OrderSearchCriteria(
        alias = "The Big Apple",
      )

      val expectedSQL = replaceWhitespace(
        baseQuery + """
            upper(alias) = upper('The Big Apple')
        """.trimIndent(),
      )

      val result: AthenaQuery = OrderService.searchKeyOrderInformationQuery(criteria)

      Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    }

    @Test
    fun `returns valid SQL if legacySubjectId and alias are populated`() {
      val criteria = OrderSearchCriteria(
        legacySubjectId = "4",
        alias = "The Big Apple",
      )

      val expectedSQL = replaceWhitespace(
        baseQuery + """
            legacy_subject_id = 4
            OR upper(alias) = upper('The Big Apple')
        """.trimIndent(),
      )

      val result: AthenaQuery = OrderService.searchKeyOrderInformationQuery(criteria)

      Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    }

    @Test
    fun `Throws error if search criteria are null`() {
      val criteria = OrderSearchCriteria()

      Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
        OrderService.searchKeyOrderInformationQuery(criteria)
      }.withMessage("At least one search criteria must be populated")
    }

    @Test
    fun `Throws error if legacySubjectId cannot be parsed as a Long`() {
      val criteria = OrderSearchCriteria(
        legacySubjectId = "fake-not a number",
      )

      Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
        OrderService.searchKeyOrderInformationQuery(criteria)
      }.withMessage("Legacy_subject_id must be convertable to type Long")
    }
  }
}