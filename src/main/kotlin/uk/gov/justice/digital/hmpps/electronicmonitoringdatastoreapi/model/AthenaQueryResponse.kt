package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

data class AthenaQueryResponse<T>(
  val queryString: String,
  val athenaRole: String,
  val errorMessage: String? = null,
  val isErrored: Boolean = false,
  val queryResponse: T? = null,
)

// data class OrderQueryResponse: AthenaQueryResponse(
//  val queryMetadata: AthenaQueryResponse,
//  val queryResult: List<Order>? = null
// ) {}
//
// data class CustomQueryResponse(
//  val queryMetadata: AthenaQueryResponse,
//  val queryResult: String? = null
// )
