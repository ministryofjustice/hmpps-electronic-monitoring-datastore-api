package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.models.AthenaOrderInformationQuery

class IntegrityOrderInformationQueryBuilder :
  SqlQueryBuilder(
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
  fun withLegacySubjectId(legacySubjectId: String?): IntegrityOrderInformationQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses.put("legacy_subject_id", "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')")
    return this
  }

  override fun build(databaseName: String): AthenaOrderInformationQuery = AthenaOrderInformationQuery(getSQL(databaseName), values.toTypedArray())
}
