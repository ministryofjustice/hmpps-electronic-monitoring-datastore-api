package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.AmIncident
import java.time.LocalDateTime

data class AlcoholMonitoringIncidentEventDetails(
  val violationAlertId: String?,
  val violationAlertDateTime: LocalDateTime?,
  val violationAlertType: String?,
  val violationAlertResponseAction: String?,
  val visitRequired: String?,
  val probationInteractionRequired: String?,
  val amsInteractionRequired: String?,
  val multipleAlerts: String?,
  val additionalAlerts: String?,
) : AlcoholMonitoringEventDetails() {

  constructor(dto: AmIncident) : this (
    violationAlertId = dto.violationAlertId,
    violationAlertDateTime = nullableLocalDateTime(dto.violationAlertDate, dto.violationAlertTime),
    violationAlertType = dto.violationAlertType,
    violationAlertResponseAction = dto.violationAlertResponseAction,
    visitRequired = dto.visitRequired,
    probationInteractionRequired = dto.probationInteractionRequired,
    amsInteractionRequired = dto.amsInteractionRequired,
    multipleAlerts = dto.multipleAlerts,
    additionalAlerts = dto.additionalAlerts,
  )
}
