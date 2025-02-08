package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventDTO
import java.time.LocalDateTime

data class ContactEventDetails(
  val outcome: String?,
  val type: String,
  val reason: String,
  val channel: String,
  val userId: String?,
  val userName: String?,
  val modifiedDateTime: LocalDateTime,
) : EventDetails() {
  constructor(dto: AthenaContactEventDTO) : this (
    outcome = dto.outcome,
    type = dto.contactType,
    reason = dto.reason,
    channel = dto.channel,
    userId = dto.userId,
    userName = dto.userName,
    modifiedDateTime = LocalDateTime.parse("${dto.modifiedDate}T${dto.modifiedTime}"),
  )
}
