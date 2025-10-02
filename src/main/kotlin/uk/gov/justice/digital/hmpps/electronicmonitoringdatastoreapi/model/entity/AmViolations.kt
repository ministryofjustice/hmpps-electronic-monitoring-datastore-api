package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity

data class AmViolations(
  val legacySubjectId: String,
  val enforcementId: String?,
  val nonComplianceReason: String?,
  val nonComplianceDate: String?,
  val nonComplianceTime: String?,
  val violationAlertId: String?,
  val violationAlertDescription: String?,
  val violationEventNotificationDate: String?,
  val violationEventNotificationTime: String?,
  val actionTakenEms: String?,
  val nonComplianceOutcome: String?,
  val nonComplianceResolved: String?,
  val dateResolved: String?,
  val openClosed: String?,
  val visitRequired: String?,
)
