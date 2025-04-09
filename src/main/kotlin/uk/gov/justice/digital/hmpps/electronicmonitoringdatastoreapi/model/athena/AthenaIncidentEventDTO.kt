package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

data class AthenaIncidentEventDTO(
  val legacySubjectId: String,
  val legacyOrderId: String,
  val violationAlertType: String?,
  val violationAlertDate: String?,
  val violationAlertTime: String?,
)
