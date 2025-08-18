package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.AthenaMonitoringEventsListQuery

class IntegrityMonitoringEventsQueryBuilder :
  SqlQueryBuilder(
    "event_history",
    arrayOf(
      "legacy_subject_id",
      "event_type",
      "event_date",
      "event_time",
      "event_second",
      "process_date",
      "process_time",
      "process_second",
    ),
  ) {
  fun withLegacySubjectId(legacySubjectId: String?): IntegrityMonitoringEventsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses.put("legacy_subject_id", "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')")
    return this
  }

  override fun build(databaseName: String): AthenaMonitoringEventsListQuery = AthenaMonitoringEventsListQuery(getSQL(databaseName), values.toTypedArray())
}
