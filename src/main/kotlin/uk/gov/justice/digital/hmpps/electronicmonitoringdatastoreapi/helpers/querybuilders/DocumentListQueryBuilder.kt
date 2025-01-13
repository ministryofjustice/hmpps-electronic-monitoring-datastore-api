package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery

class DocumentListQueryBuilder {
  var legacySubjectId: String? = null

  fun withLegacySubjectId(subjectId: String): DocumentListQueryBuilder {
    legacySubjectId = subjectId
    return this
  }

  fun build(): AthenaQuery<AthenaDocumentListDTO> = AthenaQuery(
    """
        SELECT
           $legacySubjectId as legacy_order_id
    """.trimIndent(),
  )
}
