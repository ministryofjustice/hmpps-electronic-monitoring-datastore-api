package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class AmContactEventsQueryBuilderTest {

  fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")

  val baseQuery: String = """
      SELECT
        legacy_subject_id,
        legacy_order_id,
        contact_date,
        contact_time,
        inbound_or_outbound,
        from_to,
        channel,
        subject_consent_withdrawn,
        call_outcome,
        statement,
        reason_for_contact,
        outcome_of_contact,
        visit_required,
        visit_id
      FROM 
        fake_database.am_contact_history
      WHERE 
         legacy_subject_id = ?
  """.trimIndent()

  @Test
  fun `returns valid SQL if legacySubjectId is populated`() {
    val legacySubjectId = "AA122333"

    val expectedSQL = replaceWhitespace(baseQuery.trimIndent())

    val result = AmContactEventsQueryBuilder("fake_database")
      .withLegacySubjectId(legacySubjectId)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    Assertions.assertThat(result.parameters).isEqualTo(arrayOf(legacySubjectId))
  }

  @Test
  fun `throws an error if input contains SQL injection attack to show hidden records`() {
    val dangerousInput = "12345 OR 1=1--"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      AmContactEventsQueryBuilder("fake_database")
        .withLegacySubjectId(dangerousInput)
        .build()
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }

  @Test
  fun `throws an error if input contains SQL injection attack to join other tables`() {
    val dangerousInput = "' UNION SELECT username, password FROM users--"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      AmContactEventsQueryBuilder("fake_database")
        .withLegacySubjectId(dangerousInput)
        .build()
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }
}
