package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventsListQuery

class IncidentEventsQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "incident",
  arrayOf(
    "legacy_subject_id",
    "legacy_order_id",
    "violation_alert_type",
    "violation_alert_date",
    "violation_alert_time",
  ),
) {
  fun withLegacySubjectId(subjectId: String): IncidentEventsQueryBuilder {
    validateAlphanumeric(subjectId, "legacy_subject_id")

    if (subjectId.isBlank()) {
      return this
    }

    values.add(subjectId)
    whereClauses.put("legacy_subject_id", "legacy_subject_id" eq subjectId)
    return this
  }

  fun build(): AthenaIncidentEventsListQuery = AthenaIncidentEventsListQuery(getSQL(), values.toTypedArray())
}
