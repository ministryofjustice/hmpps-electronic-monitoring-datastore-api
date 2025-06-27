package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegritySuspensionOfVisitsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegritySuspensionOfVisitsDTO

@Service
class IntegritySuspensionOfVisitsRepository(
  val athenaClient: EmDatastoreClientInterface,
  @param:Value("\${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {
  fun getSuspensionOfVisits(legacySubjectId: String, restricted: Boolean): List<AthenaIntegritySuspensionOfVisitsDTO> {
    val suspensionOfVisitsQuery = IntegritySuspensionOfVisitsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(suspensionOfVisitsQuery, restricted)

    return AthenaHelper.Companion.mapTo<AthenaIntegritySuspensionOfVisitsDTO>(athenaResponse)
  }
}
