package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegritySuspensionOfVisitsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegritySuspensionOfVisitsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper

@Service
class IntegritySuspensionOfVisitsRepository(
  athenaClient: EmDatastoreClient,
) : AthenaRepository<AthenaIntegritySuspensionOfVisitsDTO>(athenaClient) {
  override fun mapTo(results: ResultSet): List<AthenaIntegritySuspensionOfVisitsDTO> = AthenaHelper.mapTo(results)

  fun findByLegacySubjectIdAndRestricted(legacySubjectId: String, restricted: Boolean): List<AthenaIntegritySuspensionOfVisitsDTO> = this.executeQuery(
    AthenaIntegritySuspensionOfVisitsQueryBuilder().withLegacySubjectId(legacySubjectId),
    restricted,
  )
}
