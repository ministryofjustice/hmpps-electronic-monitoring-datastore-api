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
  fun withLegacySubjectId(legacySubjectId: String): MonitoringEventsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isBlank()) {
      return this
    }

    values.add(legacySubjectId)
    whereClauses.put("legacy_subject_id", "legacy_subject_id" eq legacySubjectId)
    return this
  }

  fun build(): AthenaMonitoringEventsListQuery = AthenaMonitoringEventsListQuery(getSQL(), values.toTypedArray())
}
