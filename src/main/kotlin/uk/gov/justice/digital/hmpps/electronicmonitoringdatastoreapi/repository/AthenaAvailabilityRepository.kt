package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.athena.AthenaRepository

@Service
class AthenaAvailabilityRepository(
  athenaClient: EmDatastoreClient,
) : AthenaRepository<Boolean>(athenaClient, Boolean::class) {
  fun test(restricted: Boolean = false): Boolean = this.isValid(restricted)
}
