package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery

interface EmDatastoreClientInterface {
  fun getQueryResult(athenaQuery: AthenaQuery, restricted: Boolean = false): ResultSet

  fun getQueryResult(queryExecutionId: String, restricted: Boolean = false): ResultSet

  fun getQueryExecutionId(athenaQuery: AthenaQuery, restricted: Boolean = false): String
}
