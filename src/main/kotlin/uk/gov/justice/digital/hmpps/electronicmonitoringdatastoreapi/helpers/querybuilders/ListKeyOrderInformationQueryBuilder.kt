package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchQuery

class ListKeyOrderInformationQueryBuilder :
  SqlQueryBuilder(
    "order_details",
    arrayOf(
      "legacy_subject_id",
    ),
  ) {
  override fun build(databaseName: String): AthenaOrderSearchQuery = AthenaOrderSearchQuery(getSQL(databaseName), values.toTypedArray())
}
