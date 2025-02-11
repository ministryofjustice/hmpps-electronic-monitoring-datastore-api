package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventDTO
import java.time.LocalDateTime

data class ContactEventDetails(
  val outcome: String?,
  val type: String?,
  val reason: String?,
  val channel: String?,
  val userId: String?,
  val userName: String?,
  val modifiedDateTime: LocalDateTime?,
) : EventDetails() {
  companion object {
    private fun asNullableDateTime(date: String?, time: String?): LocalDateTime? = if (date != null) {
      LocalDateTime.parse("${date}T${time ?: "00:00:00"}")
    } else {
      null
    }
  }

  constructor(dto: AthenaContactEventDTO) : this (
    outcome = dto.outcome,
    type = dto.contactType,
    reason = dto.reason,
    channel = dto.channel,
    userId = dto.userId,
    userName = dto.userName,
    modifiedDateTime = asNullableDateTime(dto.modifiedDate, dto.modifiedTime),
  )
}
