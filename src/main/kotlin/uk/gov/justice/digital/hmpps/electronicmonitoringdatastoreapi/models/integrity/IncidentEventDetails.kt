package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.integrity

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.EventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.integrity.AthenaIntegrityIncidentEventDTO

data class IncidentEventDetails(
  val type: String?,
) : EventDetails() {
  constructor(dto: AthenaIntegrityIncidentEventDTO) : this (
    type = dto.violationAlertType,
  )
}
