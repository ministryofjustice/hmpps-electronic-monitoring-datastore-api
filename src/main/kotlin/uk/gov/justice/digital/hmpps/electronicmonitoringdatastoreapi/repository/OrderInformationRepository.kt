package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.DocumentListQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.KeyOrderInformationQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SubjectHistoryReportQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSubjectHistoryReportDTO

@Service
class OrderInformationRepository(
  @Autowired val athenaClient: EmDatastoreClientInterface,
) {
  fun getKeyOrderInformation(orderId: String, role: AthenaRole): AthenaKeyOrderInformationDTO {
    val keyOrderInformationQuery = KeyOrderInformationQueryBuilder()
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(keyOrderInformationQuery, role)

    val result = AthenaHelper.mapTo<AthenaKeyOrderInformationDTO>(athenaResponse)

    return result.first()
  }

  fun getSubjectHistoryReport(orderId: String, role: AthenaRole): AthenaSubjectHistoryReportDTO {
    val subjectHistoryReportQuery = SubjectHistoryReportQueryBuilder()
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(subjectHistoryReportQuery, role)

    val result = AthenaHelper.mapTo<AthenaSubjectHistoryReportDTO>(athenaResponse)

    return result.first()
  }

  fun getDocumentList(orderId: String, role: AthenaRole): AthenaDocumentListDTO {
    val documentListQuery = DocumentListQueryBuilder()
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(documentListQuery, role)

    val result = AthenaHelper.mapTo<AthenaDocumentDTO>(athenaResponse)

    return AthenaDocumentListDTO(
      pageSize = result.size,
      orderDocuments = result,
    )
  }
}
