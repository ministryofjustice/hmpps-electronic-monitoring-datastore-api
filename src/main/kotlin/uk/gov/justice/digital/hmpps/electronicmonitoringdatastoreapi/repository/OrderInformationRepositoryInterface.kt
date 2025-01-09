package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSubjectHistoryReportDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRole

interface OrderInformationRepositoryInterface {
  fun getKeyOrderInformation(athenaQuery: AthenaQuery, role: AthenaRole): AthenaKeyOrderInformationDTO
  fun getSubjectHistoryReport(query: AthenaQuery, role: AthenaRole): AthenaSubjectHistoryReportDTO
  fun getDocumentList(query: AthenaQuery, role: AthenaRole): AthenaDocumentListDTO
}