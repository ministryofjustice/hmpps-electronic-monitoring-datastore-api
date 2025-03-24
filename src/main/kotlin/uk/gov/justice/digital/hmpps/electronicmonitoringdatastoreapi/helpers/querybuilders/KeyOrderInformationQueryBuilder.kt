package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationQuery

class KeyOrderInformationQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "order_details",
  arrayOf(
    "legacy_subject_id",
    "legacy_order_id",
    "full_name",
    "alias",
    "date_of_birth",
    "primary_address_line_1",
    "primary_address_line_2",
    "primary_address_line_3",
    "primary_address_post_code",
    "order_start_date",
    "order_end_date",
  ),
) {
  fun withLegacySubjectId(subjectId: String): KeyOrderInformationQueryBuilder {
    validateAlphanumeric(subjectId, "legacy_subject_id")

    if (subjectId.isBlank()) {
      return this
    }

    values.add(subjectId)
    whereClauses.put("legacy_subject_id", "legacy_subject_id" eq subjectId)
    return this
  }

  fun build(): AthenaKeyOrderInformationQuery = AthenaKeyOrderInformationQuery(getSQL(), values.toTypedArray())
}
