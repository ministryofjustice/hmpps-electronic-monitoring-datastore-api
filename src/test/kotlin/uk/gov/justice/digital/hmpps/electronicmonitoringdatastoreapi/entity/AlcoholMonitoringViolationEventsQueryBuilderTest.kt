package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class AlcoholMonitoringViolationEventsQueryBuilderTest {

  fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")

  val baseQuery: String = """
      SELECT
        action_taken_ems, date_resolved, enforcement_id, legacy_subject_id, non_compliance_date, non_compliance_outcome, non_compliance_reason, non_compliance_resolved, non_compliance_time, open_closed, violation_alert_description, violation_alert_id, violation_event_notification_date, violation_event_notification_time, visit_required
      FROM 
        fake_database.am_violations
      WHERE 
         UPPER(CAST(legacy_subject_id as varchar)) = ?
  """.trimIndent()

  @Test
  fun `returns valid SQL if legacySubjectId is populated`() {
    val legacySubjectId = "AA122333"

    val expectedSQL = replaceWhitespace(baseQuery.trimIndent())

    val result = AthenaAlcoholMonitoringViolationEventsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)
      .build("fake_database")

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    Assertions.assertThat(result.parameters).isEqualTo(arrayOf("UPPER('$legacySubjectId')"))
  }

  @Test
  fun `throws an error if input contains SQL injection attack to show hidden records`() {
    val dangerousInput = "12345 OR 1=1--"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      AthenaAlcoholMonitoringViolationEventsQueryBuilder()
        .withLegacySubjectId(dangerousInput)
        .build("fake_database")
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }

  @Test
  fun `throws an error if input contains SQL injection attack to join other tables`() {
    val dangerousInput = "' UNION SELECT username, password FROM users--"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      AthenaAlcoholMonitoringViolationEventsQueryBuilder()
        .withLegacySubjectId(dangerousInput)
        .build("fake_database")
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }
}
