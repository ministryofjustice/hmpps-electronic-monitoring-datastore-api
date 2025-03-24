package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class OrderSearchQueryBuilderTest {
  fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")

  val baseQuery: String = """
        SELECT 
          legacy_subject_id
          , full_name
          , alias
          , primary_address_line_1
          , primary_address_line_2
          , primary_address_line_3
          , primary_address_post_code
          , date_of_birth
          , order_start_date
          , order_end_date 
        FROM 
          test_database.integrity
        WHERE 
  """.trimIndent()

  @Test
  fun `returns valid SQL if only legacySubjectId is populated`() {
    val tableName = "integrity"
    val legacySubjectId = "111222333"

    val expectedSQL = replaceWhitespace(
      baseQuery + """CAST(legacy_subject_id AS VARCHAR)='$legacySubjectId'""".trimIndent(),
    )

    val result = OrderSearchQueryBuilder("test_database")
      .withTableName(tableName)
      .withLegacySubjectId(legacySubjectId)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
  }

  @Test
  fun `returns valid SQL if only firstName is populated`() {
    val tableName = "integrity"
    val firstName = "Steeevooooo"

    val expectedSQL = replaceWhitespace(
      baseQuery + """
            first_name LIKE UPPER('%$firstName%')
      """.trimIndent(),
    )

    val result = OrderSearchQueryBuilder("test_database")
      .withTableName(tableName)
      .withFirstName(firstName)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
  }

  @Test
  fun `throws an error if firstName contains dangerous characters`() {
    val tableName = "integrity"
    val dangerousInput: String = "Steve OR 1=1"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      OrderSearchQueryBuilder("test_database")
        .withTableName(tableName)
        .withFirstName(dangerousInput)
        .build()
    }.withMessage("Input contains illegal characters")
  }

  @Test
  fun `returns valid SQL if only LastName is populated`() {
    val tableName = "integrity"
    val lastName = "Jobs"

    val expectedSQL = replaceWhitespace(
      baseQuery + """
            last_name LIKE UPPER('%$lastName%')
      """.trimIndent(),
    )

    val result = OrderSearchQueryBuilder("test_database")
      .withTableName(tableName)
      .withLastName(lastName)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
  }

  @Test
  fun `throws an error if LastName contains dangerous characters`() {
    val tableName = "integrity"
    val dangerousInput: String = "Jobs OR 1=1"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      OrderSearchQueryBuilder("test_database")
        .withTableName(tableName)
        .withLastName(dangerousInput)
        .build()
    }.withMessage("Input contains illegal characters")
  }

  @Test
  fun `returns valid SQL if only alias is populated`() {
    val tableName = "integrity"
    val alias = "The Big Apple"

    val expectedSQL = replaceWhitespace(
      baseQuery + """
            alias LIKE UPPER('%$alias%')
      """.trimIndent(),
    )

    val result = OrderSearchQueryBuilder("test_database")
      .withTableName(tableName)
      .withAlias(alias)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
  }

  @Test
  fun `throws an error if alias contains dangerous characters`() {
    val tableName = "integrity"
    val dangerousInput: String = "Jobs OR 1=1"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      OrderSearchQueryBuilder("test_database")
        .withTableName(tableName)
        .withAlias(dangerousInput)
        .build()
    }.withMessage("Input contains illegal characters")
  }

  @Test
  fun `returns valid SQL if legacySubjectId and alias are populated`() {
    val tableName = "integrity"
    val legacySubjectId = "4"
    val alias = "The Big Apple"

    val expectedSQL = replaceWhitespace(
      baseQuery + """
            CAST(legacy_subject_id AS VARCHAR)='$legacySubjectId'
            AND alias LIKE UPPER('%$alias%')
      """.trimIndent(),
    )

    val result = OrderSearchQueryBuilder("test_database")
      .withTableName(tableName)
      .withLegacySubjectId(legacySubjectId)
      .withAlias(alias)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
  }

  @Test
  fun `Throws error if search criteria are null`() {
    val tableName = "integrity"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      OrderSearchQueryBuilder()
        .withTableName(tableName)
        .build()
    }.withMessage("At least one search criteria must be populated")
  }
}
