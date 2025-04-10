package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaAmServicesListQuery
import kotlin.collections.toTypedArray

class AmIncidentEventsQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "am_incident",
  arrayOf(
    "legacy_subject_id",
    "violation_alert_id",
    "violation_alert_date",
    "violation_alert_time",
    "violation_alert_type",
    "violation_alert_response_action",
    "visit_required",
    "probation_interaction_required",
    "ams_interaction_required",
    "multiple_alerts",
    "additional_alerts",
  ),
) {
  fun withLegacySubjectId(legacySubjectId: String): AmIncidentEventsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isBlank()) {
      return this
    }

    values.add(legacySubjectId)
    whereClauses.put("legacy_subject_id", "legacy_subject_id" eq legacySubjectId)
    return this
  }

  fun build(): AthenaAmServicesListQuery = AthenaAmServicesListQuery(getSQL(), values.toTypedArray())
}
