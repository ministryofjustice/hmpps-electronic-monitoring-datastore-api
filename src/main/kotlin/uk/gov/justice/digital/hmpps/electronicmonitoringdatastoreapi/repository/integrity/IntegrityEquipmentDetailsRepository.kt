package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityEquipmentDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityEquipmentDetailsDTO

@Service
class IntegrityEquipmentDetailsRepository(
  val athenaClient: EmDatastoreClientInterface,
  @param:Value($$"${services.athena.database}") var athenaDatabase: String = "unknown_database",
) {
  fun getEquipmentDetails(legacySubjectId: String, restricted: Boolean): List<AthenaIntegrityEquipmentDetailsDTO> {
    val equipmentDetailsQuery = IntegrityEquipmentDetailsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(equipmentDetailsQuery, restricted)

    return AthenaHelper.Companion.mapTo<AthenaIntegrityEquipmentDetailsDTO>(athenaResponse)
  }
}
