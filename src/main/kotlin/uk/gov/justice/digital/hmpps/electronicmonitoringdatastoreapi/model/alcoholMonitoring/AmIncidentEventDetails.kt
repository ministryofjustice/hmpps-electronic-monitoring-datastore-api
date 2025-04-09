package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmIncidentEventDTO
import java.time.LocalDateTime

data class AmIncidentEventDetails(
  val violationAlertId: String?,
  val violationAlertDateTime: LocalDateTime?,
  val violationAlertType: String?,
  val violationAlertResponseAction: String?,
  val visitRequired: String?,
  val probationInteractionRequired: String?,
  val amsInteractionRequired: String?,
  val multipleAlerts: String?,
  val additionalAlerts: String?,
) : AmEventDetails() {

  constructor(dto: AthenaAmIncidentEventDTO) : this (
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
