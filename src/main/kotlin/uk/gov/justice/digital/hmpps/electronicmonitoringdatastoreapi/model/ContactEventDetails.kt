package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import java.time.LocalDateTime

data class ContactEventDetails(
  val outcome: String?,
  val contactType: String,
  val reason: String,
  val channel: String,
  val userId: String?,
  val userName: String?,
  val modifiedDateTime: LocalDateTime,
) : EventDetails()
