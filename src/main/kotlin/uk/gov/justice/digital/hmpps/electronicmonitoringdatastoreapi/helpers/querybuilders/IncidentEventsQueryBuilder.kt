package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventsListQuery

class IncidentEventsQueryBuilder(
  var databaseName: String? = null,
) {
  var legacySubjectId: String? = null

  fun withLegacySubjectId(subjectId: String): IncidentEventsQueryBuilder {
    legacySubjectId = subjectId
    return this
  }

  fun build(): AthenaIncidentEventsListQuery = AthenaIncidentEventsListQuery(
    """
      SELECT
        legacy_subject_id
      , legacy_order_id
      , violation_alert_type
      , violation_alert_date
      , violation_alert_time
      FROM
        $databaseName.incident
      WHERE
        legacy_subject_id = $legacySubjectId
    """.trimIndent(),
    arrayOf(),
  )
}
