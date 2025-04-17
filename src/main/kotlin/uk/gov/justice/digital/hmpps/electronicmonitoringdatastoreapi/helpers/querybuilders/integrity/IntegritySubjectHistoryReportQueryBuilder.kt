package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSubjectHistoryReportQuery

class IntegritySubjectHistoryReportQueryBuilder(
  override var databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "subject_history_report",
  arrayOf(
    "legacy_subject_id",
    "legacy_order_id",
  ),
) {
  fun withLegacySubjectId(subjectId: String): IntegritySubjectHistoryReportQueryBuilder {
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
