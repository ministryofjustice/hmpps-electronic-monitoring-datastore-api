package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery

interface AthenaClientInterface {
  fun getQueryResult(athenaQuery: AthenaQuery, role: AthenaRole?): ResultSet
}
