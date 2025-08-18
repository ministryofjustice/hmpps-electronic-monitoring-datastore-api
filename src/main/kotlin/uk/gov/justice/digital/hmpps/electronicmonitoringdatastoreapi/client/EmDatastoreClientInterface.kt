package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder

interface EmDatastoreClientInterface {
  fun getQueryResult(athenaQuery: SqlQueryBuilder, restricted: Boolean = false): ResultSet

  fun getQueryResult(queryExecutionId: String, restricted: Boolean = false): ResultSet

  fun getQueryExecutionId(athenaQuery: SqlQueryBuilder, restricted: Boolean = false): String
}
