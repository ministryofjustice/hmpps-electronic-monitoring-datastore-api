package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AlcoholMonitoringVisitDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.alcoholMonitoring.AthenaAlcoholMonitoringVisitDetailsDTO

@Service
class AlcoholMonitoringVisitDetailsRepository(
  val athenaClient: EmDatastoreClientInterface,
) {
  fun findByLegacySubjectId(legacySubjectId: String): List<AthenaAlcoholMonitoringVisitDetailsDTO> {
    val amVisitDetailsQuery = AlcoholMonitoringVisitDetailsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(amVisitDetailsQuery)

    return AthenaHelper.mapTo<AthenaAlcoholMonitoringVisitDetailsDTO>(athenaResponse)
  }
}
