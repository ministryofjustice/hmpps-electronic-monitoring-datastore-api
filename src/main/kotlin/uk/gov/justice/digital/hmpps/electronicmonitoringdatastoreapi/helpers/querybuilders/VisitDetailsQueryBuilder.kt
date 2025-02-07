package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaVisitDetailsListQuery

class VisitDetailsQueryBuilder(
  var databaseName: String? = null,
) {
  var legacySubjectId: String? = null

  fun withLegacySubjectId(subjectId: String): VisitDetailsQueryBuilder {
    legacySubjectId = subjectId
    return this
  }

  fun build(): AthenaVisitDetailsListQuery = AthenaVisitDetailsListQuery(
    """
      SELECT
          legacy_subject_id
        , legacy_order_id
        , address_1
        , address_2
        , address_3
        , postcode
        , actual_work_start_date
        , actual_work_start_time
        , actual_work_end_date
        , actual_work_end_time
        , visit_notes
        , visit_type
        , visit_outcome
      FROM
        $databaseName.visit_details
      WHERE
        legacy_subject_id = $legacySubjectId
    """.trimIndent(),
  )
}
