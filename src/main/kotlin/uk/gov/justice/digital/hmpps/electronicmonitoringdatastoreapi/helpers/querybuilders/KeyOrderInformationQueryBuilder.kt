package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationQuery

class KeyOrderInformationQueryBuilder(
  var databaseName: String? = null,
) {
  var legacySubjectId: String? = null

  fun withLegacySubjectId(subjectId: String): KeyOrderInformationQueryBuilder {
    legacySubjectId = subjectId
    return this
  }

  fun build(): AthenaKeyOrderInformationQuery = AthenaKeyOrderInformationQuery(
    """
      SELECT
            legacy_subject_id
          , legacy_order_id
          , full_name
          , alias
          , date_of_birth
          , primary_address_line_1
          , primary_address_line_2
          , primary_address_line_3
          , primary_address_post_code
          , order_start_date
          , order_end_date
      FROM 
        $databaseName.order_details
      WHERE
        legacy_subject_id = $legacySubjectId
    """.trimIndent(),
    arrayOf(),
  )
}
