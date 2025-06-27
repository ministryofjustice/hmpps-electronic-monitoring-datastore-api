package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AmEquipmentDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmEquipmentDetailsDTO

@Service
class AmEquipmentDetailsRepository(
  val athenaClient: EmDatastoreClientInterface,
  @param:Value($$"${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {
  fun getEquipmentDetails(legacySubjectId: String): List<AthenaAmEquipmentDetailsDTO> {
    val amEquipmentDetailsQuery = AmEquipmentDetailsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(amEquipmentDetailsQuery)

    return AthenaHelper.Companion.mapTo<AthenaAmEquipmentDetailsDTO>(athenaResponse)
  }
}
