package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.AvailabilityQueryBuilder

@Service
class AvailabilityRepository(
  @field:Autowired val athenaClient: EmDatastoreClientInterface,
) {

  fun listLegacyIds(restricted: Boolean): List<String> {
    val athenaQuery = AvailabilityQueryBuilder()

    val athenaResponse: ResultSet = athenaClient.getQueryResult(athenaQuery, restricted)

    data class SubjectId(
      val legacySubjectId: String,
    )

    val result = AthenaHelper.mapTo<SubjectId>(athenaResponse)

    return result.map { it.legacySubjectId }
  }
}
