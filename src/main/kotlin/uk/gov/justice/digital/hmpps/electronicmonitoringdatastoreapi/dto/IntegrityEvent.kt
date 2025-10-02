package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto

import java.time.LocalDateTime

data class IntegrityEvent<T : IntegrityEventDetails>(
  val legacySubjectId: String,
  val type: String,
  val dateTime: LocalDateTime,
  val details: T,
)
