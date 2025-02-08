package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventDTO

data class IncidentEventDetails(
  val type: String,
) : EventDetails() {
  constructor(dto: AthenaIncidentEventDTO) : this (
    type = dto.violationAlertType,
  )
}
