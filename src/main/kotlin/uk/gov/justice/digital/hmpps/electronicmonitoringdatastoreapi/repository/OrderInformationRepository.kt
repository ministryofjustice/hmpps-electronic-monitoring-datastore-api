package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.DocumentsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.KeyOrderInformationQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SubjectHistoryReportQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSubjectHistoryReportDTO

@Service
class OrderInformationRepository(
  @Autowired val athenaClient: EmDatastoreClientInterface,
  @Value("\${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {
  fun getKeyOrderInformation(legacySubjectId: String, allowSpecials: Boolean): AthenaKeyOrderInformationDTO {
    val keyOrderInformationQuery = KeyOrderInformationQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(keyOrderInformationQuery, allowSpecials)

    val result = AthenaHelper.mapTo<AthenaKeyOrderInformationDTO>(athenaResponse)

    return result.first()
  }

  fun getSubjectHistoryReport(orderId: String, allowSpecials: Boolean): AthenaSubjectHistoryReportDTO {
    val subjectHistoryReportQuery = SubjectHistoryReportQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(subjectHistoryReportQuery, allowSpecials)

    val result = AthenaHelper.mapTo<AthenaSubjectHistoryReportDTO>(athenaResponse)

    return result.first()
  }

  fun getDocumentList(orderId: String, allowSpecials: Boolean): List<AthenaDocumentDTO> {
    val documentListQuery = DocumentsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(documentListQuery, allowSpecials)

    return AthenaHelper.mapTo<AthenaDocumentDTO>(athenaResponse)
  }
}
