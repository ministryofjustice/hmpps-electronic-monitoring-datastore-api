package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaAlcoholMonitoringEquipmentDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaAlcoholMonitoringEquipmentDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper

@Service
class AlcoholMonitoringEquipmentDetailsRepository(
  athenaClient: EmDatastoreClient,
) : AthenaRepository<AthenaAlcoholMonitoringEquipmentDetailsDTO>(athenaClient) {
  override fun mapTo(results: ResultSet): List<AthenaAlcoholMonitoringEquipmentDetailsDTO> = AthenaHelper.mapTo(results)

  fun findByLegacySubjectId(legacySubjectId: String): List<AthenaAlcoholMonitoringEquipmentDetailsDTO> = this.executeQuery(
    AthenaAlcoholMonitoringEquipmentDetailsQueryBuilder().withLegacySubjectId(legacySubjectId),
  )
}
