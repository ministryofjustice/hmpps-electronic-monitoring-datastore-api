package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.apache.commons.lang3.StringUtils.isAlphanumeric
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
    if (!isAlphanumeric(subjectId)) {
      throw IllegalArgumentException("Input contains illegal characters")
    }

    parameters["legacy_subject_id"] = subjectId
    return this
  }

  fun build(): AthenaKeyOrderInformationQuery = AthenaKeyOrderInformationQuery(getSQL(), parameters.values.toTypedArray())
}
