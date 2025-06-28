package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityOrderInformationQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityOrderInformationDTO

@Service
class IntegrityOrderInformationRepository(
  val athenaClient: EmDatastoreClientInterface,
) {
  fun getOrderInformation(legacySubjectId: String, restricted: Boolean): AthenaIntegrityOrderInformationDTO {
    val keyOrderInformationQuery = IntegrityOrderInformationQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(keyOrderInformationQuery, restricted)

    val result = AthenaHelper.mapTo<AthenaIntegrityOrderInformationDTO>(athenaResponse)

    return result.first()
  }
}
