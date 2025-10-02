package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.AmViolations
import java.time.LocalDateTime

data class AlcoholMonitoringViolationEventDetails(
  val enforcementId: String?,
  val nonComplianceReason: String?,
  val nonComplianceDateTime: LocalDateTime?,
  val violationAlertId: String?,
  val violationAlertDescription: String?,
  val violationEventNotificationDateTime: LocalDateTime?,
  val actionTakenEms: String?,
  val nonComplianceOutcome: String?,
  val nonComplianceResolved: String?,
  val dateResolved: LocalDateTime?,
  val openClosed: String?,
  val visitRequired: String?,
) : AlcoholMonitoringEventDetails() {

  constructor(dto: AmViolations) : this (
    enforcementId = dto.enforcementId,
    nonComplianceReason = dto.nonComplianceReason,
    nonComplianceDateTime = nullableLocalDateTime(dto.nonComplianceDate, dto.nonComplianceTime),
    violationAlertId = dto.violationAlertId,
    violationAlertDescription = dto.violationAlertDescription,
    violationEventNotificationDateTime = nullableLocalDateTime(dto.violationEventNotificationDate, dto.violationEventNotificationTime),
    actionTakenEms = dto.actionTakenEms,
    nonComplianceOutcome = dto.nonComplianceOutcome,
    nonComplianceResolved = dto.nonComplianceResolved,
    dateResolved = nullableLocalDateTime(dto.dateResolved),
    openClosed = dto.openClosed,
    visitRequired = dto.visitRequired,
  )
}
