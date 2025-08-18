package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models

import java.time.LocalDateTime

data class Event<T : EventDetails>(
  val legacySubjectId: String,
  val type: String,
  val dateTime: LocalDateTime,
  val details: T,
)

abstract class EventDetails
