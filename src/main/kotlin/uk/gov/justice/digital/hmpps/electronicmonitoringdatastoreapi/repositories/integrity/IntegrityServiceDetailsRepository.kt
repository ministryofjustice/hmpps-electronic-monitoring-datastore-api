package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.integrity

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityServiceDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.integrity.AthenaIntegrityServiceDetailsDTO

@Service
class IntegrityServiceDetailsRepository(
  val athenaClient: EmDatastoreClientInterface,
) {
  fun getServiceDetails(legacySubjectId: String, restricted: Boolean): List<AthenaIntegrityServiceDetailsDTO> {
    val servicesQuery = IntegrityServiceDetailsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(servicesQuery, restricted)

    return AthenaHelper.mapTo<AthenaIntegrityServiceDetailsDTO>(athenaResponse)
  }
}
