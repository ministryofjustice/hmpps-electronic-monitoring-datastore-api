package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test

class AthenaIntegrityOrderDetailsQueryBuilderTest {

  fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")

  val baseQuery: String = """
      SELECT
        adult_or_child, alias, contact, date_of_birth, false_limb_risk, first_name, last_name, legacy_order_id, legacy_subject_id, manual_risk, mappa, migrated_risk, notifying_organisation_details_name, offence_risk, order_end_date, order_start_date, order_type, order_type_description, order_type_detail, phone_or_mobile_number, post_code_risk, ppo, primary_address_line_1, primary_address_line_2, primary_address_line_3, primary_address_post_code, range_risk, report_risk, responsible_organisation, responsible_organisation_details_region, sex, technical_bail, wearing_wrist_pid
      FROM 
        fake_database.order_details
      WHERE 
         UPPER(CAST(legacy_subject_id as varchar)) = ?
  """.trimIndent()

  @Test
  fun `returns valid SQL if legacySubjectId is populated`() {
    val legacySubjectId = "111222333"

    val expectedSQL = replaceWhitespace(baseQuery.trimIndent())

    val result = AthenaIntegrityOrderDetailsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)
      .build("fake_database")

    assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    assertThat(result.parameters).isEqualTo(arrayOf("UPPER('$legacySubjectId')"))
  }

  @Test
  fun `throws an error if input contains SQL injection attack to show hidden records`() {
    val dangerousInput = "12345 OR 1=1--"

    assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      AthenaIntegrityOrderDetailsQueryBuilder()
        .withLegacySubjectId(dangerousInput)
        .build("fake_database")
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }

  @Test
  fun `throws an error if input contains SQL injection attack to join other tables`() {
    val dangerousInput = "' UNION SELECT username, password FROM users--"

    assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      AthenaIntegrityOrderDetailsQueryBuilder()
        .withLegacySubjectId(dangerousInput)
        .build("fake_database")
    }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
  }
}
