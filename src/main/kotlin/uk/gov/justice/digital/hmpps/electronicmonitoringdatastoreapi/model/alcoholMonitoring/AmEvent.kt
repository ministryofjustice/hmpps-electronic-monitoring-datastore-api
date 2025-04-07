package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring

import java.time.LocalDateTime

data class AmEvent<T : AmEventDetails>(
  val legacyOrderId: String,
  val legacySubjectId: String,
  val type: String,
  val dateTime: LocalDateTime,
  val details: T,
)

abstract class AmEventDetails
