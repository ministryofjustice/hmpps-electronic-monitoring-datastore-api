package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class AthenaIntegrityVisitDetailsQueryBuilderTest {

  fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")

  val baseQuery: String = """
      SELECT
        actual_work_end_date, actual_work_end_time, actual_work_start_date, actual_work_start_time, address_1, address_2, address_3, legacy_subject_id, postcode, visit_notes, visit_outcome, visit_type
      FROM 
        fake_database.visit_details
      WHERE 
         UPPER(CAST(legacy_subject_id as varchar)) = ?
  """.trimIndent()

  @Test
  fun `returns valid SQL if legacySubjectId is populated`() {
    val legacySubjectId = "111222333"

    val expectedSQL = replaceWhitespace(baseQuery.trimIndent())

    val result = AthenaIntegrityVisitDetailsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)
      .build("fake_database")

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    Assertions.assertThat(result.parameters).isEqualTo(arrayOf("UPPER('$legacySubjectId')"))
  }

  @Test
  fun `throws an error if input contains SQL injection attack to show hidden records`() {
    val dangerousInput = "12345 OR 1=1--"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      AthenaIntegrityVisitDetailsQueryBuilder()
        .withLegacySubjectId(dangerousInput)
        .build("fake_database")
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }

  @Test
  fun `throws an error if input contains SQL injection attack to join other tables`() {
    val dangerousInput = "' UNION SELECT username, password FROM users--"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      AthenaIntegrityVisitDetailsQueryBuilder()
        .withLegacySubjectId(dangerousInput)
        .build("fake_database")
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }
}
