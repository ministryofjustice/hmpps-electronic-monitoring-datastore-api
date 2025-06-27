package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AmVisitDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmVisitDetailsDTO

@Service
class AmVisitDetailsRepository(
  val athenaClient: EmDatastoreClientInterface,
  @param:Value($$"${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {
  fun getVisitDetails(legacySubjectId: String): List<AthenaAmVisitDetailsDTO> {
    val amVisitDetailsQuery = AmVisitDetailsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(amVisitDetailsQuery)

    return AthenaHelper.Companion.mapTo<AthenaAmVisitDetailsDTO>(athenaResponse)
  }
}
