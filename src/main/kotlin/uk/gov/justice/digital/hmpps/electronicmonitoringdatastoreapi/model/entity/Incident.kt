package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity

data class Incident(
  val legacySubjectId: String,
  val violationAlertType: String?,
  val violationAlertDate: String?,
  val violationAlertTime: String?,
)
