package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.DocumentListQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.KeyOrderInformationQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SubjectHistoryReportQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSubjectHistoryReportDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaService

@Service
@Profile("!test")
class OrderInformationRepository(
  @Autowired val athenaClient: AthenaService,
) : OrderInformationRepositoryInterface {
  override fun getKeyOrderInformation(orderId: String, role: AthenaRole): AthenaKeyOrderInformationDTO {
    val keyOrderInformationQuery = KeyOrderInformationQueryBuilder()
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(role, keyOrderInformationQuery)

    val result = AthenaHelper.mapTo<AthenaKeyOrderInformationDTO>(athenaResponse)

    return result.first()
  }

  override fun getSubjectHistoryReport(orderId: String, role: AthenaRole): AthenaSubjectHistoryReportDTO {
    val subjectHistoryReportQuery = SubjectHistoryReportQueryBuilder()
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(role, subjectHistoryReportQuery)

    val result = AthenaHelper.mapTo<AthenaSubjectHistoryReportDTO>(athenaResponse)

    return result.first()
  }

  override fun getDocumentList(orderId: String, role: AthenaRole): AthenaDocumentListDTO {
    val documentListQuery = DocumentListQueryBuilder()
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(role, documentListQuery)

    val result = AthenaHelper.mapTo<AthenaDocumentListDTO>(athenaResponse)

    return result.first()
  }
}
