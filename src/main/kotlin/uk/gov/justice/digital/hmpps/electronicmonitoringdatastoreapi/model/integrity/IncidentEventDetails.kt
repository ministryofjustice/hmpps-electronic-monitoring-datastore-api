package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.EventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityIncidentEventDTO

data class IncidentEventDetails(
  val type: String?,
) : EventDetails() {
  constructor(dto: AthenaIntegrityIncidentEventDTO) : this (
    type = dto.violationAlertType,
  )
}
