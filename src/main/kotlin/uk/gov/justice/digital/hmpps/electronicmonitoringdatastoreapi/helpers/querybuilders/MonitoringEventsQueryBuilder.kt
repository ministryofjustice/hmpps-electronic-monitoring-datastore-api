package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventsListQuery

class MonitoringEventsQueryBuilder(
  var databaseName: String? = null,
) {
  var legacySubjectId: String? = null

  fun withLegacySubjectId(subjectId: String): MonitoringEventsQueryBuilder {
    legacySubjectId = subjectId
    return this
  }

  fun build(): AthenaMonitoringEventsListQuery = AthenaMonitoringEventsListQuery(
    """
      SELECT
          legacy_subject_id
        , legacy_order_id
        , event_type
        , event_date
        , event_time
        , event_second
        , process_date
        , process_time
        , process_second
      FROM 
        $databaseName.event_history
      WHERE
        legacy_subject_id = $legacySubjectId
    """.trimIndent(),
  )
}
