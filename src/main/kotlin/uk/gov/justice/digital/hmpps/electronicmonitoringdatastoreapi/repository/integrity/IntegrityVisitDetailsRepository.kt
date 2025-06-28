package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityVisitDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityVisitDetailsDTO

@Service
class IntegrityVisitDetailsRepository(
  val athenaClient: EmDatastoreClientInterface,
) {
  fun getVisitDetails(legacySubjectId: String, restricted: Boolean): List<AthenaIntegrityVisitDetailsDTO> {
    val visitDetailsQuery = IntegrityVisitDetailsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(visitDetailsQuery, restricted)

    return AthenaHelper.Companion.mapTo<AthenaIntegrityVisitDetailsDTO>(athenaResponse)
  }
}
