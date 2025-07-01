package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AmVisitDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmVisitDetailsDTO

@Service
class AmVisitDetailsRepository(
  val athenaClient: EmDatastoreClientInterface,
) {
  fun getVisitDetails(legacySubjectId: String): List<AthenaAmVisitDetailsDTO> {
    val amVisitDetailsQuery = AmVisitDetailsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(amVisitDetailsQuery)

    return AthenaHelper.mapTo<AthenaAmVisitDetailsDTO>(athenaResponse)
  }
}
