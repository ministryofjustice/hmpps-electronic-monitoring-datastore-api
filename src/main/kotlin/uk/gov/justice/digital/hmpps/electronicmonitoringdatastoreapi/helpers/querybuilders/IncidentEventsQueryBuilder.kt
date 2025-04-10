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
    "violation_alert_type",
    "violation_alert_date",
    "violation_alert_time",
  ),
) {
  fun withLegacySubjectId(legacySubjectId: String): IncidentEventsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isBlank()) {
      return this
    }

    values.add(legacySubjectId)
    whereClauses.put("legacy_subject_id", "legacy_subject_id" eq legacySubjectId)
    return this
  }

  fun build(): AthenaIncidentEventsListQuery = AthenaIncidentEventsListQuery(getSQL(), values.toTypedArray())
}
