package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity

data class AthenaIntegrityMonitoringEventDTO(
  val legacySubjectId: String,
  val eventType: String?,
  val eventDate: String?,
  val eventTime: String?,
  val eventSecond: Int?,
  val processDate: String?,
  val processTime: String?,
  val processSecond: Int?,
)
