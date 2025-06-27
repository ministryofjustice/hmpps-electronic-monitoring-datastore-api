package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AmOrderInformationQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmOrderInformationDTO

@Service
class AmOrderInformationRepository(
  val athenaClient: EmDatastoreClientInterface,
) {
  fun getOrderInformation(legacySubjectId: String): AthenaAmOrderInformationDTO {
    val orderInformationQuery = AmOrderInformationQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(orderInformationQuery)

    val result = AthenaHelper.mapTo<AthenaAmOrderInformationDTO>(athenaResponse)

    return result.first()
  }
}
