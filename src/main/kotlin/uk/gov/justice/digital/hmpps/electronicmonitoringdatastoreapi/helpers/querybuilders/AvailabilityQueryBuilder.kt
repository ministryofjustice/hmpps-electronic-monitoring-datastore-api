package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchQuery

class AvailabilityQueryBuilder :
  SqlQueryBuilder(
    "order_details",
    arrayOf(
      "1",
    ),
  ) {
  override fun build(databaseName: String): AthenaOrderSearchQuery = AthenaOrderSearchQuery(getSQL(databaseName), values.toTypedArray())
}
