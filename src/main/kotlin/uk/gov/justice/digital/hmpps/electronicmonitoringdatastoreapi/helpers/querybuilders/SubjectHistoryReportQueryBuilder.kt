package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

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
    parameters["legacy_subject_id"] = subjectId
    return this
  }

  fun build(): AthenaSubjectHistoryReportQuery = AthenaSubjectHistoryReportQuery(getSQL(), parameters.values.toTypedArray())
}
