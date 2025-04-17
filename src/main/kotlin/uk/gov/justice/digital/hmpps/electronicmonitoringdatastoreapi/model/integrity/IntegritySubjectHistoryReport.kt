package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegritySubjectHistoryReportDTO

data class IntegritySubjectHistoryReport(
  val reportUrl: String,
  val name: String,
  val createdOn: String,
  val time: String,
) {
  companion object {
    fun createEmpty(): IntegritySubjectHistoryReport = IntegritySubjectHistoryReport("", "", "", "")
  }

  constructor(dto: AthenaIntegritySubjectHistoryReportDTO) : this (
    reportUrl = dto.reportUrl,
    name = dto.name,
    createdOn = dto.createdOn,
    time = dto.time,
  )
}
