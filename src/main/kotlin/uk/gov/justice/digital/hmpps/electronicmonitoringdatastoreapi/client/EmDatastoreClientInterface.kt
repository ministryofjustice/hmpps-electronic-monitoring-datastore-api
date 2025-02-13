package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.ResultSetAndId

interface EmDatastoreClientInterface {
  fun getQueryResult(athenaQuery: AthenaQuery, role: AthenaRole?): ResultSet

  fun getResultFromQueryString(athenaQuery: AthenaQuery, role: AthenaRole?): ResultSetAndId

  fun getResultFromExecutionId(executionId: String, role: AthenaRole?): ResultSetAndId
}
