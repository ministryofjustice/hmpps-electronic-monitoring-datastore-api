package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.EventHistory
import java.time.LocalDateTime

data class IntegrityMonitoringEventDetails(
  val type: String?,
  val processedDateTime: LocalDateTime?,
) : IntegrityEventDetails() {

  constructor(dto: EventHistory) : this(
    type = dto.eventType,
    processedDateTime = nullableLocalDateTime(dto.processDate, dto.processTime),
  )
}
