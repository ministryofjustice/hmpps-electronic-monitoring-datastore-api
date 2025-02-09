package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventDTO
import java.time.LocalDateTime

data class MonitoringEventDetails(
  val type: String?,
  val processedDateTime: LocalDateTime?,
) : EventDetails() {
  companion object {
    private fun asNullableDateTime(date: String?, time: String?): LocalDateTime? = if (date != null) {
      LocalDateTime.parse("${date}T${time ?: "00:00:00"}")
    } else {
      null
    }
  }

  constructor(dto: AthenaMonitoringEventDTO) : this(
    type = dto.eventType,
    processedDateTime = asNullableDateTime(dto.processDate, dto.processTime),
  )
}
