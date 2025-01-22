package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import java.time.LocalDateTime

data class Event<T : EventDetails>(
  val legacyOrderId: Int,
  val legacySubjectId: Int,
  val type: String,
  val dateTime: LocalDateTime,
  val details: T,
)

abstract class EventDetails
