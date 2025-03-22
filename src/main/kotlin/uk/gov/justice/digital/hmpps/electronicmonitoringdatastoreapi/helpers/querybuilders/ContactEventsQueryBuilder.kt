package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventsListQuery

class ContactEventsQueryBuilder(
  var databaseName: String? = null,
) {
  var legacySubjectId: String? = null

  fun withLegacySubjectId(subjectId: String): ContactEventsQueryBuilder {
    legacySubjectId = subjectId
    return this
  }

  fun build(): AthenaContactEventsListQuery = AthenaContactEventsListQuery(
    """
      SELECT
        legacy_subject_id
        , legacy_order_id
        , outcome
        , contact_type
        , reason
        , channel
        , user_id
        , user_name
        , contact_date
        , contact_time
        , modified_date
        , modified_time
      FROM
        $databaseName.contact_history
      WHERE
        legacy_subject_id = $legacySubjectId
    """.trimIndent(),
    arrayOf(),
  )
}
