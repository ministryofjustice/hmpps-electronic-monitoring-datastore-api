package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventDTO
import java.time.LocalDateTime

data class MonitoringEventDetails(
  val type: String?,
  val processedDateTime: LocalDateTime?,
) : EventDetails() {

  constructor(dto: AthenaMonitoringEventDTO) : this(
    type = dto.eventType,
    processedDateTime = nullableLocalDateTime(dto.processDate, dto.processTime),
  )
}
