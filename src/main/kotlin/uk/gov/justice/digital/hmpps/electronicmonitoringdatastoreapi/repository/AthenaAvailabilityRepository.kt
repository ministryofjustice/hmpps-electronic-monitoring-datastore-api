package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaAvailabilityQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper

@Service
class AthenaAvailabilityRepository(
  athenaClient: EmDatastoreClient,
) : AthenaRepository<Boolean>(athenaClient) {
  override fun mapTo(results: ResultSet): List<Boolean> = AthenaHelper.mapTo(results)

  fun test(restricted: Boolean = false): Boolean = this.executeQuery(
    AthenaAvailabilityQueryBuilder(),
    restricted,
  ).isNotEmpty()
}
