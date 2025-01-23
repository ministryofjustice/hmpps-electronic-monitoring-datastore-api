package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderDetailsQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery

@Service
class OrderDetailsRepository(
  @Autowired val athenaClient: EmDatastoreClientInterface,
) {
  fun getOrderDetails(orderId: String, role: AthenaRole): AthenaOrderDetailsDTO {
    val orderDetailsQuery: AthenaQuery = AthenaOrderDetailsQuery("fakequerystring")
//    val orderDetailsQuery: AthenaOrderDetailsQuery = OrderDetailsQueryBuilder()
//      .withLegacySubjectId(orderId)
//      .build()

    val athenaResponse = athenaClient.getQueryResult(orderDetailsQuery, role)

    val result = AthenaHelper.mapTo<AthenaOrderDetailsDTO>(athenaResponse)

    return result.first()
  }
}
