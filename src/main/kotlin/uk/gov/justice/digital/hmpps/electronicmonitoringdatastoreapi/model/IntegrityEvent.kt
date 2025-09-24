package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import java.time.LocalDateTime

data class IntegrityEvent<T : IntegrityEventDetails>(
  val legacySubjectId: String,
  val type: String,
  val dateTime: LocalDateTime,
  val details: T,
)
