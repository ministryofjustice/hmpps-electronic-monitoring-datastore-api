package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.ServicesQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaResultListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaServicesDTO

@Service
class OrderServicesRepository(
  @Autowired val athenaClient: EmDatastoreClientInterface,
  @Value("\${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {
  fun getServicesList(orderId: String, role: AthenaRole): AthenaResultListDTO<AthenaServicesDTO> {
    val servicesQuery = ServicesQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(servicesQuery, role)

    val result = AthenaHelper.Companion.mapTo<AthenaServicesDTO>(athenaResponse)

    return AthenaResultListDTO(
      pageSize = result.size,
      items = result,
    )
  }
}
