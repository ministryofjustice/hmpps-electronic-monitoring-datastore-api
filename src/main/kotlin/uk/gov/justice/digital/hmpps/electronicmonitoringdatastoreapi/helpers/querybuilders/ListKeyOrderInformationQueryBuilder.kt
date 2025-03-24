package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchQuery

class ListKeyOrderInformationQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "order_details",
  arrayOf(
    "legacy_subject_id",
  ),
) {
  fun build(): AthenaOrderSearchQuery = AthenaOrderSearchQuery(getSQL(), values.toTypedArray())
}
