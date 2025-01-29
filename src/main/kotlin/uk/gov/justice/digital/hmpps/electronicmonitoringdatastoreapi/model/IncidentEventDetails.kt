package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventDTO

data class IncidentEventDetails(
  val violation: String,
) : EventDetails() {
  constructor(dto: AthenaIncidentEventDTO) : this (
    violation = dto.violationAlertType,
  )
}
