package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class MonitoringEventsQueryBuilderTest {

  fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")

  val baseQuery: String = """
      SELECT 
        legacy_subject_id, 
        legacy_order_id, 
        event_type, 
        event_date, 
        event_time, 
        event_second, 
        process_date, 
        process_time, 
        process_second 
      FROM 
        fake_database.event_history 
      WHERE 
        legacy_subject_id = ?
  """.trimIndent()

  @Test
  fun `returns valid SQL if legacySubjectId is populated`() {
    val legacySubjectId = "111222333"

    val expectedSQL = replaceWhitespace(baseQuery.trimIndent())

    val result = MonitoringEventsQueryBuilder("fake_database")
      .withLegacySubjectId(legacySubjectId)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    Assertions.assertThat(result.parameters).isEqualTo(arrayOf(legacySubjectId))
  }

  @Test
  fun `throws an error if input contains SQL injection attack to show hidden records`() {
    val dangerousInput = "12345 OR 1=1--"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      MonitoringEventsQueryBuilder("fake_database")
        .withLegacySubjectId(dangerousInput)
        .build()
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }

  @Test
  fun `throws an error if input contains SQL injection attack to join other tables`() {
    val dangerousInput = "' UNION SELECT username, password FROM users--"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      MonitoringEventsQueryBuilder("fake_database")
        .withLegacySubjectId(dangerousInput)
        .build()
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }
}
