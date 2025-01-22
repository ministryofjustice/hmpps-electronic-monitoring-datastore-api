package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventListDTO
import java.time.LocalDateTime

data class MonitoringEventList(
  val pageSize: Int,
  val events: List<Event<MonitoringEventDetails>>,
) {
  constructor(dto: AthenaMonitoringEventListDTO) : this (
    pageSize = dto.pageSize,
    events = dto.events.map { event ->
      Event<MonitoringEventDetails>(
        legacyOrderId = event.legacyOrderId,
        legacySubjectId = event.legacySubjectId,
        type = event.eventType,
        dateTime = LocalDateTime.parse("${event.eventDate}T${event.eventTime}"),
        details = MonitoringEventDetails(
          processedDateTime = LocalDateTime.parse("${event.processDate}T${event.processTime}"),
        ),
      )
    },
  )
}
