package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.EventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.models.integrity.AthenaIntegrityMonitoringEventDTO
import java.time.LocalDateTime

data class IntegrityMonitoringEventDetails(
  val type: String?,
  val processedDateTime: LocalDateTime?,
) : EventDetails() {

  constructor(dto: AthenaIntegrityMonitoringEventDTO) : this(
    type = dto.eventType,
    processedDateTime = nullableLocalDateTime(dto.processDate, dto.processTime),
  )
}
