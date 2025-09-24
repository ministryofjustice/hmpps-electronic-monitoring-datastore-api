package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity

data class AthenaAlcoholMonitoringIncidentEventDTO(
  val legacySubjectId: String,
  val violationAlertId: String?,
  val violationAlertDate: String?,
  val violationAlertTime: String?,
  val violationAlertType: String?,
  val violationAlertResponseAction: String?,
  val visitRequired: String?,
  val probationInteractionRequired: String?,
  val amsInteractionRequired: String?,
  val multipleAlerts: String?,
  val additionalAlerts: String?,
)
