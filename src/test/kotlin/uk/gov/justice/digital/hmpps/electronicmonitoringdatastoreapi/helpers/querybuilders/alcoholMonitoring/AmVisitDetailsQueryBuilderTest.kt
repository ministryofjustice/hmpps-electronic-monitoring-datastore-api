package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class AmVisitDetailsQueryBuilderTest {

  fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")

  val baseQuery: String = """
      SELECT
        legacy_subject_id,
        visit_id,
        visit_type,
        visit_attempt,
        date_visit_raised,
        visit_address,
        visit_notes,
        visit_outcome,
        actual_work_start_date,
        actual_work_start_time,
        actual_work_end_date,
        actual_work_end_time,
        visit_rejection_reason,
        visit_rejection_description,
        visit_cancel_reason,
        visit_cancel_description
      FROM 
        fake_database.am_visit_details
      WHERE 
         legacy_subject_id = ?
  """.trimIndent()

  @Test
  fun `returns valid SQL if legacySubjectId is populated`() {
    val legacySubjectId = "AA122333"

    val expectedSQL = replaceWhitespace(baseQuery.trimIndent())

    val result = AmVisitDetailsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)
      .build("fake_database")

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    Assertions.assertThat(result.parameters).isEqualTo(arrayOf(legacySubjectId))
  }

  @Test
  fun `throws an error if input contains SQL injection attack to show hidden records`() {
    val dangerousInput = "12345 OR 1=1--"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      AmVisitDetailsQueryBuilder()
        .withLegacySubjectId(dangerousInput)
        .build("fake_database")
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }

  @Test
  fun `throws an error if input contains SQL injection attack to join other tables`() {
    val dangerousInput = "' UNION SELECT username, password FROM users--"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      AmVisitDetailsQueryBuilder()
        .withLegacySubjectId(dangerousInput)
        .build("fake_database")
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }
}
