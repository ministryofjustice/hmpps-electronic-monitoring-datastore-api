package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSubjectHistoryReportDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRole

interface OrderInformationRepositoryInterface {
  fun getKeyOrderInformation(orderId: String, role: AthenaRole): AthenaKeyOrderInformationDTO
  fun getSubjectHistoryReport(orderId: String, role: AthenaRole): AthenaSubjectHistoryReportDTO
  fun getDocumentList(orderId: String, role: AthenaRole): AthenaDocumentListDTO
}