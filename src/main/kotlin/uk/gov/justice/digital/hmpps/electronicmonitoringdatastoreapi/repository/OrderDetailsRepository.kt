package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.OrderDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaCapOrderDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderDetailsQuery

@Service
class OrderDetailsRepository(
  @Autowired val athenaClient: EmDatastoreClientInterface,
  @Value("\${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {

  fun getOrderDetails(orderId: String, role: AthenaRole): AthenaCapOrderDetailsDTO {
    val orderDetailsQuery: AthenaOrderDetailsQuery = OrderDetailsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(orderDetailsQuery, role)

    val result = AthenaHelper.mapTo<AthenaCapOrderDetailsDTO>(athenaResponse)

    return result.first()
  }
}
