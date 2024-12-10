package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Order
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.SearchCriteria

class OrderRepositoryTest {

  fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")

  @Test
  fun `OrderRepository can be instantiated`() {
    val sut = OrderRepository()
    Assertions.assertThat(sut).isNotNull()
  }

  @Test
  fun `GetFakeOrders() returns List of Orders`() {
    val result: List<Order> = OrderRepository.getFakeOrders()

    Assertions.assertThat(result).contains(
      Order(
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

  @Nested
  inner class ParseSearchCriteria {

    val baseQuery: String = """
        SELECT 
          legacy_subject_id, 
          full_name, 
          primary_address_line_1, 
          primary_address_line_2, 
          primary_address_line_3, 
          primary_address_post_code, 
          order_start_date, 
          order_end_date 
        FROM 
          test_database.order_details
        WHERE 
    """.trimIndent()

    @Test
    fun `returns valid SQL if only legacySubjectId is populated`() {
      val criteria = SearchCriteria(
        legacySubjectId = "111222333",
      )

      val expectedSQL = replaceWhitespace(
        baseQuery + """
            legacy_subject_id = 111222333
        """.trimIndent(),
      )

      val result: AthenaQuery = OrderRepository.parseSearchCriteria(criteria)

      Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    }

    @Test
    fun `returns valid SQL if only firstName is populated`() {
      val criteria = SearchCriteria(
        firstName = "Steeevooooo",
      )

      val expectedSQL = replaceWhitespace(
        baseQuery + """
            upper(first_name) = upper('Steeevooooo')
        """.trimIndent(),
      )

      val result: AthenaQuery = OrderRepository.parseSearchCriteria(criteria)

      Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    }

    @Test
    fun `returns valid SQL if only LastName is populated`() {
      val criteria = SearchCriteria(
        lastName = "Jobs",
      )

      val expectedSQL = replaceWhitespace(
        baseQuery + """
            upper(last_name) = upper('Jobs')
        """.trimIndent(),
      )

      val result: AthenaQuery = OrderRepository.parseSearchCriteria(criteria)

      Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    }

    @Test
    fun `returns valid SQL if only alias is populated`() {
      val criteria = SearchCriteria(
        alias = "The Big Apple",
      )

      val expectedSQL = replaceWhitespace(
        baseQuery + """
            upper(alias) = upper('The Big Apple')
        """.trimIndent(),
      )

      val result: AthenaQuery = OrderRepository.parseSearchCriteria(criteria)

      Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    }

    @Test
    fun `returns valid SQL if legacySubjectId and alias are populated`() {
      val criteria = SearchCriteria(
        legacySubjectId = "4",
        alias = "The Big Apple",
      )

      val expectedSQL = replaceWhitespace(
        baseQuery + """
            legacy_subject_id = 4
            OR upper(alias) = upper('The Big Apple')
        """.trimIndent(),
      )

      val result: AthenaQuery = OrderRepository.parseSearchCriteria(criteria)

      Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    }

    @Test
    fun `Throws error if search criteria are null`() {
      val criteria = SearchCriteria()

      Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
        OrderRepository.parseSearchCriteria(criteria)
      }.withMessage("At least one search criteria must be populated")
    }

    @Test
    fun `Throws error if legacySubjectId cannot be parsed as a Long`() {
      val criteria = SearchCriteria(
        legacySubjectId = "fake-not a number",
      )

      Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
        OrderRepository.parseSearchCriteria(criteria)
      }.withMessage("Legacy_subject_id must be convertable to type Long")
    }
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
