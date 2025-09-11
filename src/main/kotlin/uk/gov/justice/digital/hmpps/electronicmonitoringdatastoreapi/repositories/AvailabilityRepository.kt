package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.AvailabilityQueryBuilder

@Service
class AvailabilityRepository(
  @field:Autowired val athenaClient: EmDatastoreClientInterface,
) {

  fun test(restricted: Boolean): Boolean {
    val athenaQuery = AvailabilityQueryBuilder()

    val athenaResponse = athenaClient.getQueryResult(athenaQuery, restricted)

    return athenaResponse.hasRows()
  }
}
