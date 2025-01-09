package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

data class AthenaQuery(
  val queryString: String,
  val orderId: String = "legacy-order-id",
)
