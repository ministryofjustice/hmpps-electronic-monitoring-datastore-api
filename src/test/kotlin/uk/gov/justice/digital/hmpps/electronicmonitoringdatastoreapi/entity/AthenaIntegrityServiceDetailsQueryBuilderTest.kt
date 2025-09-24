package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class AthenaIntegrityServiceDetailsQueryBuilderTest {

  fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")

  val baseQuery: String = """
      SELECT 
        curfew_end_date, curfew_end_time, curfew_start_date, curfew_start_time, friday, legacy_subject_id, monday, saturday, service_address_1, service_address_2, service_address_3, service_address_postcode, service_end_date, service_id, service_start_date, sunday, thursday, tuesday, wednesday
      FROM 
        fake_database.services 
      WHERE 
        UPPER(CAST(legacy_subject_id as varchar)) = ?
  """.trimIndent()

  @Test
  fun `returns valid SQL if legacySubjectId is populated`() {
    val legacySubjectId = "111222333"

    val expectedSQL = replaceWhitespace(baseQuery.trimIndent())

    val result = AthenaIntegrityServiceDetailsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)
      .build("fake_database")

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    Assertions.assertThat(result.parameters).isEqualTo(arrayOf("UPPER('$legacySubjectId')"))
  }

  @Test
  fun `throws an error if input contains SQL injection attack to show hidden records`() {
    val dangerousInput = "12345 OR 1=1--"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      AthenaIntegrityServiceDetailsQueryBuilder()
        .withLegacySubjectId(dangerousInput)
        .build("fake_database")
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }

  @Test
  fun `throws an error if input contains SQL injection attack to join other tables`() {
    val dangerousInput = "' UNION SELECT username, password FROM users--"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      AthenaIntegrityServiceDetailsQueryBuilder()
        .withLegacySubjectId(dangerousInput)
        .build("fake_database")
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }
}
