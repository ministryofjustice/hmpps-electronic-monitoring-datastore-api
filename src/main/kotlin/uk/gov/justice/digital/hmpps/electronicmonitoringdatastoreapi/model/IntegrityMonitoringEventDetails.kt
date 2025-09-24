package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityMonitoringEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import java.time.LocalDateTime

data class IntegrityMonitoringEventDetails(
  val type: String?,
  val processedDateTime: LocalDateTime?,
) : IntegrityEventDetails() {

  constructor(dto: AthenaIntegrityMonitoringEventDTO) : this(
    type = dto.eventType,
    processedDateTime = nullableLocalDateTime(dto.processDate, dto.processTime),
  )
}
