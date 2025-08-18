package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.integrity

data class AthenaIntegrityIncidentEventDTO(
  val legacySubjectId: String,
  val violationAlertType: String?,
  val violationAlertDate: String?,
  val violationAlertTime: String?,
)
