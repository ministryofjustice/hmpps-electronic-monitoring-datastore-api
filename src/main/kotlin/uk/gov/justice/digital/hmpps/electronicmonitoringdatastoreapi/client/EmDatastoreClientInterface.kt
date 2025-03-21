package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery

interface EmDatastoreClientInterface {
  fun getQueryResult(athenaQuery: AthenaQuery, allowSpecials: Boolean? = false): ResultSet

  fun getQueryResult(queryExecutionId: String, allowSpecials: Boolean? = false): ResultSet

  fun getQueryExecutionId(athenaQuery: AthenaQuery, allowSpecials: Boolean? = false): String
}
