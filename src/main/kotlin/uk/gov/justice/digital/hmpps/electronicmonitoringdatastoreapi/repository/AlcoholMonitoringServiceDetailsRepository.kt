package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaAlcoholMonitoringServiceDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaAlcoholMonitoringServiceDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper

@Service
class AlcoholMonitoringServiceDetailsRepository(
  athenaClient: EmDatastoreClient,
) : AthenaRepository<AthenaAlcoholMonitoringServiceDetailsDTO>(athenaClient) {
  override fun mapTo(results: ResultSet): List<AthenaAlcoholMonitoringServiceDetailsDTO> = AthenaHelper.mapTo(results)

  fun findByLegacySubjectId(legacySubjectId: String): List<AthenaAlcoholMonitoringServiceDetailsDTO> = this.executeQuery(
    AthenaAlcoholMonitoringServiceDetailsQueryBuilder().withLegacySubjectId(legacySubjectId),
  )
}
