package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventListDTO
import java.time.LocalDateTime

data class IncidentEventList(
  val pageSize: Int,
  val events: List<Event<IncidentEventDetails>>,
) {
  constructor(dto: AthenaIncidentEventListDTO) : this (
    pageSize = dto.pageSize,
    events = dto.events.map { event ->
      Event<IncidentEventDetails>(
        legacyOrderId = event.legacyOrderId,
        legacySubjectId = event.legacySubjectId,
        type = "Violation alert",
        dateTime = LocalDateTime.parse("${event.violationAlertDate}T${event.violationAlertTime}"),
        details = IncidentEventDetails(
          violation = event.violationAlertType,
        ),
      )
    },
  )
}
