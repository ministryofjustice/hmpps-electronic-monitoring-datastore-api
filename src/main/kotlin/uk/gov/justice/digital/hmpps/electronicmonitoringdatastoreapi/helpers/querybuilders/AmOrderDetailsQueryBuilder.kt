package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.apache.commons.lang3.StringUtils.isAlphanumeric
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaAmOrderDetailsQuery

class AmOrderDetailsQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "am_order_details",
  arrayOf(
    "legacy_subject_id",
    "legacy_order_id",
    "first_name",
    "last_name",
    "alias",
    "date_of_birth date",
    "legacy_gender",
    "primary_address_line1",
    "primary_address_line2",
    "primary_address_line3",
    "primary_address_postcode",
    "phone_number1",
    "order_start_date date",
    "order_end_date date",
    "order_type",
    "order_type_description",
    "enforceable_condition",
    "order_end_outcome",
    "responsible_org_details_phone_number",
    "responsible_org_details_email",
    "tag_at_source",
    "special_instructions",
  ),
) {
  fun withLegacySubjectId(subjectId: String): AmOrderDetailsQueryBuilder {
    if (!isAlphanumeric(subjectId)) {
      throw IllegalArgumentException("Input contains illegal characters")
    }

    parameters["legacy_subject_id"] = subjectId
    return this
  }

  fun build(): AthenaAmOrderDetailsQuery = AthenaAmOrderDetailsQuery(getSQL(), parameters.values.toTypedArray())
}
