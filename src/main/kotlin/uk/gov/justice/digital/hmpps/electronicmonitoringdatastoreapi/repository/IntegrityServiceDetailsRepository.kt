package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityServiceDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityServiceDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper

@Service
class IntegrityServiceDetailsRepository(
  athenaClient: EmDatastoreClient,
) : AthenaRepository<AthenaIntegrityServiceDetailsDTO>(athenaClient) {
  override fun mapTo(results: ResultSet): List<AthenaIntegrityServiceDetailsDTO> = AthenaHelper.mapTo(results)

  fun findByLegacySubjectIdAndRestricted(legacySubjectId: String, restricted: Boolean): List<AthenaIntegrityServiceDetailsDTO> = this.executeQuery(
    AthenaIntegrityServiceDetailsQueryBuilder().withLegacySubjectId(legacySubjectId),
    restricted,
  )
}
