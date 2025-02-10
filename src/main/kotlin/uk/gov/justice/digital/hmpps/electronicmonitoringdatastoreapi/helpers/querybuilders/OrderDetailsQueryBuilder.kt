package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.apache.commons.lang3.StringUtils.isAlphanumeric
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderDetailsQuery

class OrderDetailsQueryBuilder(
  private val databaseName: String? = "test_database",
) {
  var legacySubjectId: String? = null

  fun withLegacySubjectId(subjectId: String): OrderDetailsQueryBuilder {
    if (!isAlphanumeric(subjectId)) {
      throw IllegalArgumentException("Input contains illegal characters")
    }

    // TODO: Consider using PreparedStatement and a library to avoid manual SQL assembly
    // val testQuery: String = "SELECT * FROM ?.my_table WHERE user_id = ?"
    // val connection: Connection = null
    // val statement: PreparedStatement = connection.prepareStatement(testQuery)

    legacySubjectId = subjectId
    return this
  }

  fun build(): AthenaOrderDetailsQuery = AthenaOrderDetailsQuery(
    """
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
        $databaseName.order_details
      WHERE
        legacy_subject_id = $legacySubjectId
    """.trimIndent(),
  )
}
