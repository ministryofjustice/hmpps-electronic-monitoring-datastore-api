package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.Incident

data class IntegrityIncidentEventDetails(
  val type: String?,
) : IntegrityEventDetails() {
  constructor(dto: Incident) : this (
    type = dto.violationAlertType,
  )
}
