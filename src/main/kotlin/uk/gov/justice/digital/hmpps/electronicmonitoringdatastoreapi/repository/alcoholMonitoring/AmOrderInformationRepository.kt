package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AmOrderInformationQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmOrderInformationDTO

@Service
class AmOrderInformationRepository(
  val athenaClient: EmDatastoreClientInterface,
  @param:Value($$"${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {
  fun getOrderInformation(legacySubjectId: String): AthenaAmOrderInformationDTO {
    val orderInformationQuery = AmOrderInformationQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(orderInformationQuery)

    val result = AthenaHelper.mapTo<AthenaAmOrderInformationDTO>(athenaResponse)

    return result.first()
  }
}
