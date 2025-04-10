package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.ServiceDetailsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaServiceDetailsDTO

@Service
class ServiceDetailsRepository(
  @Autowired val athenaClient: EmDatastoreClientInterface,
  @Value("\${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {
  fun getServiceDetails(legacySubjectId: String, role: AthenaRole): List<AthenaServiceDetailsDTO> {
    val servicesQuery = ServiceDetailsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(servicesQuery, role)

    return AthenaHelper.Companion.mapTo<AthenaServiceDetailsDTO>(athenaResponse)
  }
}
