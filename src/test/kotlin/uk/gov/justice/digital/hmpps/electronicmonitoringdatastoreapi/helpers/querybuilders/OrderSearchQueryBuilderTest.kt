package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class OrderSearchQueryBuilderTest {
  fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")

  val baseQuery: String = """
        SELECT 
          legacy_subject_id,
          legacy_order_id,
          first_name,
          last_name,
          alias,
          date_of_birth,
          primary_address_line_1,
          primary_address_line_2,
          primary_address_line_3,
          primary_address_post_code,
          order_start_date,
          order_end_date
        FROM 
          test_database.test_table
        WHERE 
  """.trimIndent()

  @Test
  fun `returns valid SQL if only legacySubjectId is populated`() {
    val legacySubjectId = "111222333"

    val expectedSQL = replaceWhitespace(
      baseQuery + """
            UPPER(CAST(legacy_subject_id as varchar)) = ?
      """.trimIndent(),
    )

    val expectedParameters = arrayOf("UPPER('111222333')")

    val result = OrderSearchQueryBuilder("test_database", "test_table")
      .withLegacySubjectId(legacySubjectId)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    Assertions.assertThat(result.parameters).isEqualTo(expectedParameters)
  }

  @Test
  fun `returns valid SQL if only firstName is populated`() {
    val firstName = "Stevo"

    val expectedSQL = replaceWhitespace(
      baseQuery + """
            first_name LIKE ?
      """.trimIndent(),
    )

    val result = OrderSearchQueryBuilder("test_database", "test_table")
      .withFirstName(firstName)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    Assertions.assertThat(result.parameters).isEqualTo(arrayOf("UPPER('%$firstName%')"))
  }

  @Test
  fun `throws an error if firstName contains dangerous characters`() {
    val dangerousInput = "Steve OR 1=1"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      OrderSearchQueryBuilder("test_database", "test_table")
        .withFirstName(dangerousInput)
        .build()
    }.withMessage("first_name must only contain alphanumeric characters and spaces")
  }

  @Test
  fun `returns valid SQL if only LastName is populated`() {
    val lastName = "Jobs"

    val expectedSQL = replaceWhitespace(
      baseQuery + """
            last_name LIKE ?
      """.trimIndent(),
    )

    val result = OrderSearchQueryBuilder("test_database", "test_table")
      .withLastName(lastName)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    Assertions.assertThat(result.parameters).isEqualTo(arrayOf("UPPER('%$lastName%')"))
  }

  @Test
  fun `throws an error if LastName contains dangerous characters`() {
    val dangerousInput = "Jobs OR 1=1"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      OrderSearchQueryBuilder("test_database", "test_table")
        .withLastName(dangerousInput)
        .build()
    }.withMessage("last_name must only contain alphanumeric characters and spaces")
  }

  @Test
  fun `returns valid SQL if only alias is populated`() {
    val alias = "The Big Apple"

    val expectedSQL = replaceWhitespace(
      baseQuery + """
            alias LIKE ?
      """.trimIndent(),
    )

    val result = OrderSearchQueryBuilder("test_database", "test_table")
      .withAlias(alias)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    Assertions.assertThat(result.parameters).isEqualTo(arrayOf("UPPER('%$alias%')"))
  }

  @Test
  fun `throws an error if alias contains dangerous characters`() {
    val dangerousInput = "Jobs OR 1=1"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      OrderSearchQueryBuilder("test_database", "test_table")
        .withAlias(dangerousInput)
        .build()
    }.withMessage("alias must only contain alphanumeric characters and spaces")
  }

  @Test
  fun `returns valid SQL if legacySubjectId and alias are populated`() {
    val legacySubjectId = "4"
    val alias = "The Big Apple"

    val expectedSQL = replaceWhitespace(
      baseQuery + """
            UPPER(CAST(legacy_subject_id as varchar)) = ?
            AND alias LIKE ?
      """.trimIndent(),
    )
    val expectedParameters = arrayOf("UPPER('4')", "UPPER('%The Big Apple%')")

    val result = OrderSearchQueryBuilder("test_database", "test_table")
      .withLegacySubjectId(legacySubjectId)
      .withAlias(alias)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    Assertions.assertThat(result.parameters).isEqualTo(expectedParameters)
  }

  @Test
  fun `Throws error if search criteria are null`() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      OrderSearchQueryBuilder("fake_database", "test_table").build()
    }.withMessage("At least one search criteria must be populated")
  }

  @Test
  fun `Throws error if legacySubjectId cannot be parsed as a Long`() {
    val illegalLegacySubjectId = "fake-not a number"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      OrderSearchQueryBuilder("test_database", "test_table").withLegacySubjectId(illegalLegacySubjectId)
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }
}
