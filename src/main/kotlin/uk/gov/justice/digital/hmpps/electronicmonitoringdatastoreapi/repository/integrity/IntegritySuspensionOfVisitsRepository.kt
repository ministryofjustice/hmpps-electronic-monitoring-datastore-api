package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegritySuspensionOfVisitsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegritySuspensionOfVisitsDTO

@Service
class IntegritySuspensionOfVisitsRepository(
  val athenaClient: EmDatastoreClientInterface,
) {
  fun getSuspensionOfVisits(legacySubjectId: String, restricted: Boolean): List<AthenaIntegritySuspensionOfVisitsDTO> {
    val suspensionOfVisitsQuery = IntegritySuspensionOfVisitsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(suspensionOfVisitsQuery, restricted)

    return AthenaHelper.mapTo<AthenaIntegritySuspensionOfVisitsDTO>(athenaResponse)
  }
}
