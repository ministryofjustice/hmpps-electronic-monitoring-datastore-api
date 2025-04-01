package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaAmOrderDetailsQuery

class AmOrderDetailsQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "am_order_details",
  arrayOf(
    "legacy_subject_id",
    "first_name",
    "last_name",
    "alias",
    "date_of_birth",
    "legacy_gender",
    "special_instructions",
    "phone_or_mobile_number",
    "primary_address_line_1",
    "primary_address_line_2",
    "primary_address_line_3",
    "primary_address_post_code",
    "legacy_order_id",
    "order_start_date",
    "order_end_date",
    "enforceable_condition",
    "order_type",
    "order_type_description",
    "order_end_outcome",
    "responsible_org_details_phone_number",
    "responsible_org_details_email",
    "tag_at_source",
  ),
) {
  fun withLegacySubjectId(legacySubjectId: String): AmOrderDetailsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isBlank()) {
      return this
    }

    values.add(legacySubjectId)
    whereClauses.put("legacy_subject_id", "legacy_subject_id" eq legacySubjectId)
    return this
  }

  fun build(): AthenaAmOrderDetailsQuery = AthenaAmOrderDetailsQuery(getSQL(), values.toTypedArray())
}
