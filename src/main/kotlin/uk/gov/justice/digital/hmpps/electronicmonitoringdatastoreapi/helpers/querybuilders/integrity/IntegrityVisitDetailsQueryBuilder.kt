package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaVisitDetailsListQuery
import kotlin.collections.toTypedArray

class IntegrityVisitDetailsQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "visit_details",
  arrayOf(
    "legacy_subject_id",
    "address_1",
    "address_2",
    "address_3",
    "postcode",
    "actual_work_start_date",
    "actual_work_start_time",
    "actual_work_end_date",
    "actual_work_end_time",
    "visit_notes",
    "visit_type",
    "visit_outcome",
  ),
) {
  fun withLegacySubjectId(legacySubjectId: String): IntegrityVisitDetailsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isBlank()) {
      return this
    }

    values.add(legacySubjectId)
    whereClauses.put("legacy_subject_id", "legacy_subject_id" eq legacySubjectId)
    return this
  }

  fun build(): AthenaVisitDetailsListQuery = AthenaVisitDetailsListQuery(getSQL(), values.toTypedArray())
}
