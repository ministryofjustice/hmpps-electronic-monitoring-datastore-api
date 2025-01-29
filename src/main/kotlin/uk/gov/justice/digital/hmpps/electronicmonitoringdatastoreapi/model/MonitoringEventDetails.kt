package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventDTO
import java.time.LocalDateTime

data class MonitoringEventDetails(
  val processedDateTime: LocalDateTime,
) : EventDetails() {
  constructor(dto: AthenaMonitoringEventDTO) : this(
    processedDateTime = LocalDateTime.parse("${dto.processDate}T${dto.processTime}"),
  )
}
