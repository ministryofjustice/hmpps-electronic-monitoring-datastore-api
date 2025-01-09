package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Document
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.DocumentList
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.KeyOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.SubjectHistoryReport
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSubjectHistoryReportDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaService

@Service
@Profile("!test")
class OrderInformationRepository(
  @Autowired val athenaClient: AthenaService
): OrderInformationRepositoryInterface {
  override fun getKeyOrderInformation(athenaQuery: AthenaQuery, role: AthenaRole): AthenaKeyOrderInformationDTO {
    val athenaResponse = athenaClient.getQueryResult(role, athenaQuery.queryString)

    val result = AthenaHelper.mapTo<AthenaKeyOrderInformationDTO>(athenaResponse)

    return result.first()
  }

  override fun getSubjectHistoryReport(athenaQuery: AthenaQuery, role: AthenaRole): AthenaSubjectHistoryReportDTO {
    val athenaResponse = athenaClient.getQueryResult(role, athenaQuery.queryString)

    val result = AthenaHelper.mapTo<AthenaSubjectHistoryReportDTO>(athenaResponse)

    return result.first()
  }

  override fun getDocumentList(athenaQuery: AthenaQuery, role: AthenaRole): AthenaDocumentListDTO {
    val athenaResponse = athenaClient.getQueryResult(role, athenaQuery.queryString)

    val result = AthenaHelper.mapTo<AthenaDocumentListDTO>(athenaResponse)

    return result.first()
  }
}
