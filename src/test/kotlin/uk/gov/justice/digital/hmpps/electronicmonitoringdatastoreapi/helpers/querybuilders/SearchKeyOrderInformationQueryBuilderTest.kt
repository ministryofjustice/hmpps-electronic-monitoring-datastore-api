package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class SearchKeyOrderInformationQueryBuilderTest {
  fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")

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
    val legacySubjectId = "111222333"

    val expectedSQL = replaceWhitespace(
      baseQuery + """
            legacy_subject_id=$legacySubjectId
      """.trimIndent(),
    )

    val result = SearchKeyOrderInformationQueryBuilder("test_database")
      .withLegacySubjectId(legacySubjectId)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
  }

  @Test
  fun `returns valid SQL if only firstName is populated`() {
    val firstName = "Steeevooooo"

    val expectedSQL = replaceWhitespace(
      baseQuery + """
            first_name=upper('$firstName')
      """.trimIndent(),
    )

    val result = SearchKeyOrderInformationQueryBuilder("test_database")
      .withFirstName(firstName)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
  }

  @Test
  fun `returns valid SQL if only LastName is populated`() {
    val lastName = "Jobs"

    val expectedSQL = replaceWhitespace(
      baseQuery + """
            last_name=upper('$lastName')
      """.trimIndent(),
    )

    val result = SearchKeyOrderInformationQueryBuilder("test_database")
      .withLastName(lastName)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
  }

  @Test
  fun `returns valid SQL if only alias is populated`() {
    val alias = "The Big Apple"

    val expectedSQL = replaceWhitespace(
      baseQuery + """
            alias=upper('$alias')
      """.trimIndent(),
    )

    val result = SearchKeyOrderInformationQueryBuilder("test_database")
      .withAlias(alias)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
  }

  @Test
  fun `returns valid SQL if legacySubjectId and alias are populated`() {
    val legacySubjectId = "4"
    val alias = "The Big Apple"

    val expectedSQL = replaceWhitespace(
      baseQuery + """
            legacy_subject_id=$legacySubjectId
            OR alias=upper('$alias')
      """.trimIndent(),
    )

    val result = SearchKeyOrderInformationQueryBuilder("test_database")
      .withLegacySubjectId(legacySubjectId)
      .withAlias(alias)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
  }

  @Test
  fun `Throws error if search criteria are null`() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      SearchKeyOrderInformationQueryBuilder().build()
    }.withMessage("At least one search criteria must be populated")
  }

  @Test
  fun `Throws error if legacySubjectId cannot be parsed as a Long`() {
    val illegalLegacySubjectId = "fake-not a number"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      SearchKeyOrderInformationQueryBuilder().withLegacySubjectId(illegalLegacySubjectId)
    }.withMessage("Legacy_subject_id must be convertable to type Long")
  }
}
