package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityIncidentEventDTO

data class IntegrityIncidentEventDetails(
  val type: String?,
) : IntegrityEventDetails() {
  constructor(dto: AthenaIntegrityIncidentEventDTO) : this (
    type = dto.violationAlertType,
  )
}
