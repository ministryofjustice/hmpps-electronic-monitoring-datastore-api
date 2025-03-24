package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSubjectHistoryReportQuery

class SubjectHistoryReportQueryBuilder(
  override var databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "subject_history_report",
  arrayOf(
    "legacy_subject_id",
    "legacy_order_id",
  ),
) {
  fun withLegacySubjectId(subjectId: String): SubjectHistoryReportQueryBuilder {
    validateAlphanumeric(subjectId, "legacy_subject_id")

    if (subjectId.isBlank()) {
      return this
    }

    values.add(subjectId)
    whereClauses.put("legacy_subject_id", "legacy_subject_id" eq subjectId)
    return this
  }

  fun build(): AthenaSubjectHistoryReportQuery = AthenaSubjectHistoryReportQuery(getSQL(), values.toTypedArray())
}
