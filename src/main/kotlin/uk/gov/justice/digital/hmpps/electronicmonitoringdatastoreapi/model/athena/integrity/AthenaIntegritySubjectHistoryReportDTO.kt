package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity

data class AthenaIntegritySubjectHistoryReportDTO(
  val reportUrl: String,
  val name: String,
  val createdOn: String,
  val time: String,
)
