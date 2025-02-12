package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

import software.amazon.awssdk.services.athena.model.ResultSet

data class AthenaQueryResponse<T>(
  val queryString: String,
  val athenaRole: String,
  val errorMessage: String? = null,
  val isErrored: Boolean = false,
  val queryResponse: T? = null,
)

data class ResultSetAndId(
  val resultSet: ResultSet,
  val queryExecutionId: String,
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
