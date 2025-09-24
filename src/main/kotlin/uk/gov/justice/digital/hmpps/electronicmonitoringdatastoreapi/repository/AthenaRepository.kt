package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.SqlQueryBuilder

abstract class AthenaRepository<T>(
  val athenaClient: EmDatastoreClient,
) {
  abstract fun mapTo(results: ResultSet): List<T>

  fun executeQuery(athenaQuery: SqlQueryBuilder, restricted: Boolean = false): List<T> {
    val queryExecutionId = athenaClient.getQueryExecutionId(athenaQuery)
    val queryResult = athenaClient.getQueryResult(queryExecutionId, restricted)
    return mapTo(queryResult)
  }
}
