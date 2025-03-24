package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventsListQuery

class MonitoringEventsQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "event_history",
  arrayOf(
    "legacy_subject_id",
    "legacy_order_id",
    "event_type",
    "event_date",
    "event_time",
    "event_second",
    "process_date",
    "process_time",
    "process_second",
  ),
) {
  fun withLegacySubjectId(subjectId: String): MonitoringEventsQueryBuilder {
    validateAlphanumeric(subjectId, "legacy_subject_id")

    if (subjectId.isBlank()) {
      return this
    }

    values.add(subjectId)
    whereClauses.put("legacy_subject_id", "legacy_subject_id" eq subjectId)
    return this
  }

  fun build(): AthenaMonitoringEventsListQuery = AthenaMonitoringEventsListQuery(getSQL(), values.toTypedArray())
}
