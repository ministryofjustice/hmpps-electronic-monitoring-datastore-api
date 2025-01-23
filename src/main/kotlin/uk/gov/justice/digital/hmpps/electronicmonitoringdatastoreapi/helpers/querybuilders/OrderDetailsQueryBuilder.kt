package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderDetailsQuery

class OrderDetailsQueryBuilder {
  var legacySubjectId: String? = null

  fun withLegacySubjectId(subjectId: String): OrderDetailsQueryBuilder {
    legacySubjectId = subjectId
    return this
  }

  fun build(): AthenaOrderDetailsQuery = AthenaOrderDetailsQuery(
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
        test_database.order_details
      WHERE
        legacy_subject_id = $legacySubjectId
    """.trimIndent(),
  )
}
