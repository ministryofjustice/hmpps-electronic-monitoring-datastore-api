package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity

data class AthenaIntegrityContactEventDTO(
  val legacySubjectId: String,
  val outcome: String?,
  val contactType: String?,
  val reason: String?,
  val channel: String?,
  val userId: String?,
  val userName: String?,
  val contactDate: String?,
  val contactTime: String?,
  val modifiedDate: String?,
  val modifiedTime: String?,
)
