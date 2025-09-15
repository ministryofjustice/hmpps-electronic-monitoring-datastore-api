package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

class AvailabilityQueryBuilder :
  SqlQueryBuilder(
    "order_details",
    arrayOf(
      "1",
    ),
  ) {
  override fun build(databaseName: String): AthenaIntegrityOrderSearchQuery = AthenaIntegrityOrderSearchQuery(getSQL(databaseName), values.toTypedArray())
}
