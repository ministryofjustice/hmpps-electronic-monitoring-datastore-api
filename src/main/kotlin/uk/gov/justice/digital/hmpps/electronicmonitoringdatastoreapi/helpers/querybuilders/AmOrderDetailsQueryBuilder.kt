package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.apache.commons.lang3.StringUtils.isAlphanumeric
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaAmOrderDetailsQuery

class AmOrderDetailsQueryBuilder(
  private val databaseName: String? = "test_database",
) {
  var legacySubjectId: String? = null

  fun withLegacySubjectId(subjectId: String): AmOrderDetailsQueryBuilder {
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

  fun build(): AthenaAmOrderDetailsQuery = AthenaAmOrderDetailsQuery(
    """
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
        $databaseName.am_order_details
      WHERE
        legacy_subject_id=$legacySubjectId
    """.trimIndent(),
    arrayOf(),
  )
}
