package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.apache.commons.lang3.StringUtils.isAlphanumeric
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.AmOrderDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaAmOrderDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaAmOrderDetailsQuery

@Service
class AmOrderDetailsRepository(
  @Autowired val athenaClient: EmDatastoreClientInterface,
) {
  fun getAmOrderDetails(orderId: String, role: AthenaRole): AthenaAmOrderDetailsDTO {
    if (!isAlphanumeric(orderId)) {
      throw IllegalArgumentException("Input contains illegal characters")
    }

    val amOrderDetailsQuery: AthenaAmOrderDetailsQuery = AmOrderDetailsQueryBuilder("fake_database")
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(amOrderDetailsQuery, role)

    val result = AthenaHelper.mapTo<AthenaAmOrderDetailsDTO>(athenaResponse)

    return result.first()
  }
}
