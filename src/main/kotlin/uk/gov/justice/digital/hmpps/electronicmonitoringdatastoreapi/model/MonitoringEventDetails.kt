package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import java.time.LocalDateTime

data class MonitoringEventDetails(
  val processedDateTime: LocalDateTime,
) : EventDetails()
