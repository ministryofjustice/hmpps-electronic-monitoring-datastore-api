package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityVisitDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityVisitDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper

@Service
class IntegrityVisitDetailsRepository(
  athenaClient: EmDatastoreClient,
) : AthenaRepository<AthenaIntegrityVisitDetailsDTO>(athenaClient) {
  override fun mapTo(results: ResultSet): List<AthenaIntegrityVisitDetailsDTO> = AthenaHelper.mapTo(results)

  fun findByLegacySubjectIdAndRestricted(legacySubjectId: String, restricted: Boolean): List<AthenaIntegrityVisitDetailsDTO> = this.executeQuery(
    AthenaIntegrityVisitDetailsQueryBuilder().withLegacySubjectId(legacySubjectId),
    restricted,
  )
}
