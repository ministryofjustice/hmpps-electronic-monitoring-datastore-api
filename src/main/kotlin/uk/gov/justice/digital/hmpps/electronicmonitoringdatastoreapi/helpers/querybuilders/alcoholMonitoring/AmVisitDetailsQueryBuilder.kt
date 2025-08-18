package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.models.AthenaAmVisitDetailsListQuery
import kotlin.collections.toTypedArray

class AmVisitDetailsQueryBuilder :
  SqlQueryBuilder(
    "am_visit_details",
    arrayOf(
      "legacy_subject_id",
      "visit_id",
      "visit_type",
      "visit_attempt",
      "date_visit_raised",
      "visit_address",
      "visit_notes",
      "visit_outcome",
      "actual_work_start_date",
      "actual_work_start_time",
      "actual_work_end_date",
      "actual_work_end_time",
      "visit_rejection_reason",
      "visit_rejection_description",
      "visit_cancel_reason",
      "visit_cancel_description",
    ),
  ) {
  fun withLegacySubjectId(legacySubjectId: String?): AmVisitDetailsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses.put("legacy_subject_id", "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')")
    return this
  }

  override fun build(databaseName: String): AthenaAmVisitDetailsListQuery = AthenaAmVisitDetailsListQuery(getSQL(databaseName), values.toTypedArray())
}
