package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSubjectHistoryReportDTO

data class SubjectHistoryReport(
  val reportUrl: String,
  val name: String,
  val createdOn: String,
  val time: String,
) {
  companion object {
    fun createEmpty(): SubjectHistoryReport = SubjectHistoryReport("", "", "", "")
  }

  constructor(dto: AthenaSubjectHistoryReportDTO) : this (
    reportUrl = dto.reportUrl,
    name = dto.name,
    createdOn = dto.createdOn,
    time = dto.time,
  )
}
