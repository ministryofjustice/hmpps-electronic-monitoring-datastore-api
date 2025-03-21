package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class OrderDetailsQueryBuilderTest {

  fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")

  val baseQuery: String = """
      SELECT
          legacy_subject_id
        , legacy_order_id
        , first_name
        , last_name
        , alias
        , date_of_birth
        , adult_or_child
        , sex
        , contact
        , primary_address_line_1
        , primary_address_line_2
        , primary_address_line_3
        , primary_address_post_code
        , phone_or_mobile_number
        , ppo
        , mappa
        , technical_bail
        , manual_risk
        , offence_risk
        , post_code_risk
        , false_limb_risk
        , migrated_risk
        , range_risk
        , report_risk
        , order_start_date
        , order_end_date
        , order_type
        , order_type_description
        , order_type_detail
        , wearing_wrist_pid
        , notifying_organisation_details_name
        , responsible_organisation
        , responsible_organisation_details_region
      FROM 
        test_database.order_details
      WHERE 
  """.trimIndent()

  @Test
  fun `returns valid SQL if legacySubjectId is populated`() {
    val legacySubjectId = "111222333"

    val expectedSQL = replaceWhitespace(
      baseQuery + """
            legacy_subject_id = $legacySubjectId
      """.trimIndent(),
    )

    val result = OrderDetailsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
  }

  @Test
  fun `throws an error if input contains dangerous characters`() {
    val dangerousInput: String = "12345 OR 1=1"

    Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
      OrderDetailsQueryBuilder()
        .withLegacySubjectId(dangerousInput)
        .build()
    }.withMessage("Input contains illegal characters")
  }
}
