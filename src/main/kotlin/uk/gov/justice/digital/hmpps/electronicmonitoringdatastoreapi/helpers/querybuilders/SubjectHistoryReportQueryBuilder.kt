package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSubjectHistoryReportQuery

class SubjectHistoryReportQueryBuilder(
  var databaseName: String? = null,
) {
  var legacySubjectId: String? = null

  fun withLegacySubjectId(subjectId: String): SubjectHistoryReportQueryBuilder {
    legacySubjectId = subjectId
    return this
  }

  fun build(): AthenaSubjectHistoryReportQuery = AthenaSubjectHistoryReportQuery(
    """
        SELECT
           $legacySubjectId as legacy_order_id
    """.trimIndent(),
    arrayOf(),
  )
}
