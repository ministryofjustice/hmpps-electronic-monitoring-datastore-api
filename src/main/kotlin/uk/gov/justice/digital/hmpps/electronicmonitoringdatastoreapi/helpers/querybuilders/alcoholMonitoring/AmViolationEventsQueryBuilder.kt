package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaAmServicesListQuery
import kotlin.collections.toTypedArray

class AmViolationEventsQueryBuilder :
  SqlQueryBuilder(
    "am_violations",
    arrayOf(
      "legacy_subject_id",
      "enforcement_id",
      "non_compliance_reason",
      "non_compliance_date",
      "non_compliance_time",
      "violation_alert_id",
      "violation_alert_description",
      "violation_event_notification_date",
      "violation_event_notification_time",
      "action_taken_ems",
      "non_compliance_outcome",
      "non_compliance_resolved",
      "date_resolved",
      "open_closed",
      "visit_required",
    ),
  ) {
  fun withLegacySubjectId(legacySubjectId: String?): AmViolationEventsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses.put("legacy_subject_id", "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')")
    return this
  }

  override fun build(databaseName: String): AthenaAmServicesListQuery = AthenaAmServicesListQuery(getSQL(databaseName), values.toTypedArray())
}
