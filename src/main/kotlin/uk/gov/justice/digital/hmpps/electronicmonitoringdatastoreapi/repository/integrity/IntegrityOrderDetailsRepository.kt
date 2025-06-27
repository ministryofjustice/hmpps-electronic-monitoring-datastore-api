package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityOrderDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityOrderDetailsDTO

@Service
class IntegrityOrderDetailsRepository(
  val athenaClient: EmDatastoreClientInterface,
) {
  fun getOrderDetails(legacySubjectId: String, restricted: Boolean): AthenaIntegrityOrderDetailsDTO {
    val orderDetailsQuery = IntegrityOrderDetailsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(orderDetailsQuery, restricted)

    val result = AthenaHelper.mapTo<AthenaIntegrityOrderDetailsDTO>(athenaResponse)

    return result.first()
  }
}
