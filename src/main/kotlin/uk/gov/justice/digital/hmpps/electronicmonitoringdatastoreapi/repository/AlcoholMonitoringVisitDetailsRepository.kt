package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaAlcoholMonitoringVisitDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaAlcoholMonitoringVisitDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper

@Service
class AlcoholMonitoringVisitDetailsRepository(
  athenaClient: EmDatastoreClient,
) : AthenaRepository<AthenaAlcoholMonitoringVisitDetailsDTO>(athenaClient) {
  override fun mapTo(results: ResultSet): List<AthenaAlcoholMonitoringVisitDetailsDTO> = AthenaHelper.mapTo(results)

  fun findByLegacySubjectId(legacySubjectId: String): List<AthenaAlcoholMonitoringVisitDetailsDTO> = this.executeQuery(
    AthenaAlcoholMonitoringVisitDetailsQueryBuilder().withLegacySubjectId(legacySubjectId),
  )
}
