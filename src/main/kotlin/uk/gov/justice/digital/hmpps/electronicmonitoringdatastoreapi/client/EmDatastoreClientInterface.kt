package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery

interface EmDatastoreClientInterface {
  fun getQueryResult(athenaQuery: AthenaQuery, restricted: Boolean): ResultSet
  fun getQueryResult(athenaQuery: AthenaQuery, role: AthenaRole?): ResultSet

  fun getQueryResult(queryExecutionId: String, restricted: Boolean): ResultSet
  fun getQueryResult(queryExecutionId: String, role: AthenaRole?): ResultSet

  fun getQueryExecutionId(athenaQuery: AthenaQuery, restricted: Boolean): String
  fun getQueryExecutionId(athenaQuery: AthenaQuery, role: AthenaRole?): String
}
