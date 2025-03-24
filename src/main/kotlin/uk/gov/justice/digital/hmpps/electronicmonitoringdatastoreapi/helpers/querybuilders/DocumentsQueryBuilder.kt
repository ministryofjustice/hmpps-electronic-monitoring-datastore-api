package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentListQuery

class DocumentsQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "equipment_details",
  arrayOf(
    "legacy_subject_id",
    "legacy_order_id",
  ),
) {
  fun withLegacySubjectId(subjectId: String): DocumentsQueryBuilder {
    validateAlphanumeric(subjectId, "legacy_subject_id")

    if (subjectId.isBlank()) {
      return this
    }

    values.add(subjectId)
    whereClauses.put("legacy_subject_id", "legacy_subject_id" eq subjectId)
    return this
  }

  fun build(): AthenaDocumentListQuery = AthenaDocumentListQuery(getSQL(), values.toTypedArray())
}
