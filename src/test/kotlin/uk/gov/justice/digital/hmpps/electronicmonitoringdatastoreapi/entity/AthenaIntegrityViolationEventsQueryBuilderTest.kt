package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class AthenaIntegrityViolationEventsQueryBuilderTest {

  fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")

  val baseQuery: String = """
      SELECT 
        agency_action, agency_response_date, authority_first_notified_date, authority_first_notified_time, breach_date, breach_details, breach_enforcement_outcome, breach_identified_date, breach_identified_time, breach_pack_requested_date, breach_pack_sent_date, breach_time, enforcement_reason, hearing_date, investigation_outcome_reason, legacy_subject_id, section_9_date, subject_letter_sent_date, summons_served_date, warning_letter_sent_date, warning_letter_sent_time
      FROM 
        fake_database.violations 
      WHERE 
        UPPER(CAST(legacy_subject_id as varchar)) = ?
  """.trimIndent()

  @Test
  fun `returns valid SQL if legacySubjectId is populated`() {
    val legacySubjectId = "111222333"

    val expectedSQL = replaceWhitespace(baseQuery.trimIndent())

    val result = AthenaIntegrityViolationEventsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)
      .build("fake_database")

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    Assertions.assertThat(result.parameters).isEqualTo(arrayOf("UPPER('$legacySubjectId')"))
  }

  @Test
  fun `throws an error if input contains SQL injection attack to show hidden records`() {
    val dangerousInput = "12345 OR 1=1--"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      AthenaIntegrityViolationEventsQueryBuilder()
        .withLegacySubjectId(dangerousInput)
        .build("fake_database")
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }

  @Test
  fun `throws an error if input contains SQL injection attack to join other tables`() {
    val dangerousInput = "' UNION SELECT username, password FROM users--"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      AthenaIntegrityViolationEventsQueryBuilder()
        .withLegacySubjectId(dangerousInput)
        .build("fake_database")
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }
}
