package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.AthenaIncidentEventsListQuery

class IntegrityIncidentEventsQueryBuilder :
  SqlQueryBuilder(
    "incident",
    arrayOf(
      "legacy_subject_id",
      "violation_alert_type",
      "violation_alert_date",
      "violation_alert_time",
    ),
  ) {
  fun withLegacySubjectId(legacySubjectId: String?): IntegrityIncidentEventsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses.put("legacy_subject_id", "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')")
    return this
  }

  override fun build(databaseName: String): AthenaIncidentEventsListQuery = AthenaIncidentEventsListQuery(getSQL(databaseName), values.toTypedArray())
}
