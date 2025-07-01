package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class AmOrderInformationQueryBuilderTest {

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
        fake_database.am_order_details 
      WHERE 
        UPPER(CAST(legacy_subject_id as varchar)) = ?
  """.trimIndent()

  @Test
  fun `returns valid SQL if legacySubjectId is populated`() {
    val legacySubjectId = "AA122333"

    val expectedSQL = replaceWhitespace(baseQuery.trimIndent())

    val result = AmOrderInformationQueryBuilder()
      .withLegacySubjectId(legacySubjectId)
      .build("fake_database")

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    Assertions.assertThat(result.parameters).isEqualTo(arrayOf("UPPER('$legacySubjectId')"))
  }

  @Test
  fun `throws an error if input contains SQL injection attack to show hidden records`() {
    val dangerousInput = "12345 OR 1=1--"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      AmOrderInformationQueryBuilder()
        .withLegacySubjectId(dangerousInput)
        .build("fake_database")
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }

  @Test
  fun `throws an error if input contains SQL injection attack to join other tables`() {
    val dangerousInput = "' UNION SELECT username, password FROM users--"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      AmOrderInformationQueryBuilder()
        .withLegacySubjectId(dangerousInput)
        .build("fake_database")
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }
}
