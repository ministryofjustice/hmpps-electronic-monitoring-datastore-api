package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaAlcoholMonitoringIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
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

  constructor(dto: AthenaAlcoholMonitoringIncidentEventDTO) : this (
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
