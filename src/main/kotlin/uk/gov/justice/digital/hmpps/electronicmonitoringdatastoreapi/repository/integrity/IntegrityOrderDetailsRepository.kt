package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityOrderDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderDetailsQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityOrderDetailsDTO

@Service
class IntegrityOrderDetailsRepository(
  @Autowired val athenaClient: EmDatastoreClientInterface,
  @Value("\${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {
  fun getOrderDetails(legacySubjectId: String, role: AthenaRole): AthenaIntegrityOrderDetailsDTO {
    val orderDetailsQuery: AthenaOrderDetailsQuery = IntegrityOrderDetailsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(orderDetailsQuery, role)

    val result = AthenaHelper.mapTo<AthenaIntegrityOrderDetailsDTO>(athenaResponse)

    return result.first()
  }
}
