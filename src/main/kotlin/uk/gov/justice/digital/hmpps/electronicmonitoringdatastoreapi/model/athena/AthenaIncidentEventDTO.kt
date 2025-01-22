package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

data class AthenaIncidentEventDTO(
  val legacySubjectId: Int,
  val legacyOrderId: Int,
  val violationAlertType: String,
  val violationAlertDate: String,
  val violationAlertTime: String,
)
