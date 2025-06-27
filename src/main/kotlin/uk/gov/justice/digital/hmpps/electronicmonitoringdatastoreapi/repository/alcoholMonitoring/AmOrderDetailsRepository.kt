package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AmOrderDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmOrderDetailsDTO

@Service
class AmOrderDetailsRepository(
  val athenaClient: EmDatastoreClientInterface,
  @param:Value("\${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {
  fun getOrderDetails(legacySubjectId: String): AthenaAmOrderDetailsDTO {
    val orderDetailsQuery = AmOrderDetailsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(orderDetailsQuery)

    val result = AthenaHelper.mapTo<AthenaAmOrderDetailsDTO>(athenaResponse)

    return result.first()
  }
}
