package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.DocumentsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.OrderInformationQueryBuilder
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
  fun getKeyOrderInformation(legacySubjectId: String, role: AthenaRole): AthenaKeyOrderInformationDTO {
    val keyOrderInformationQuery = OrderInformationQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(keyOrderInformationQuery, role)

    val result = AthenaHelper.mapTo<AthenaKeyOrderInformationDTO>(athenaResponse)

    return result.first()
  }

  fun getSubjectHistoryReport(orderId: String, role: AthenaRole): AthenaSubjectHistoryReportDTO {
    val subjectHistoryReportQuery = SubjectHistoryReportQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(subjectHistoryReportQuery, role)

    val result = AthenaHelper.mapTo<AthenaSubjectHistoryReportDTO>(athenaResponse)

    return result.first()
  }

  fun getDocumentList(orderId: String, role: AthenaRole): List<AthenaDocumentDTO> {
    val documentListQuery = DocumentsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(documentListQuery, role)

    return AthenaHelper.mapTo<AthenaDocumentDTO>(athenaResponse)
  }
}
