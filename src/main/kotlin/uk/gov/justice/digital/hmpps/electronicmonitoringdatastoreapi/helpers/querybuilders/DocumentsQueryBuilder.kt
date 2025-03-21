package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentListQuery

class DocumentsQueryBuilder(
  var databaseName: String? = null,
) {
  var legacySubjectId: String? = null

  fun withLegacySubjectId(subjectId: String): DocumentsQueryBuilder {
    legacySubjectId = subjectId
    return this
  }

  fun build(): AthenaDocumentListQuery = AthenaDocumentListQuery(
    """
      SELECT
         $legacySubjectId as legacy_order_id
    """.trimIndent(),
  )
}
