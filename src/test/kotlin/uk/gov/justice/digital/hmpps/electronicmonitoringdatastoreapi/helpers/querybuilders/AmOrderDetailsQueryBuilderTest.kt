package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class AmOrderDetailsQueryBuilderTest {

  fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")

  val baseQuery: String = """
      SELECT
        id
      , legacy_order_id
      , legacy_subject_id
      , first_name
      , last_name
      , alias
      , date_of_birth date
      , legacy_gender
      , primary_address_line1
      , primary_address_line2
      , primary_address_line3
      , primary_address_postcode
      , phone_number1
      , order_start_date date
      , order_end_date date
      , order_type
      , order_type_description
      , enforceable_condition
      , order_end_outcome
      , responsible_org_details_phone_number
      , responsible_org_details_email
      , tag_at_source
      , special_instructions
      FROM 
        test_database.am_order_details
      WHERE 
  """.trimIndent()

  @Test
  fun `returns valid SQL if legacySubjectId is populated`() {
    val legacySubjectId = "111222333"

    val expectedSQL = replaceWhitespace(
      baseQuery + """
            legacy_subject_id=$legacySubjectId
      """.trimIndent(),
    )

    val result = AmOrderDetailsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
  }

  @Test
  fun `throws an error if input contains dangerous characters`() {
    val dangerousInput: String = "12345 OR 1=1"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      AmOrderDetailsQueryBuilder()
        .withLegacySubjectId(dangerousInput)
        .build()
    }.withMessage("Input contains illegal characters")
  }
}
