package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityContactEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import java.time.LocalDateTime

data class IntegrityContactEventDetails(
  val outcome: String?,
  val type: String?,
  val reason: String?,
  val channel: String?,
  val userId: String?,
  val userName: String?,
  val modifiedDateTime: LocalDateTime?,
) : IntegrityEventDetails() {

  constructor(dto: AthenaIntegrityContactEventDTO) : this (
    outcome = dto.outcome,
    type = dto.contactType,
    reason = dto.reason,
    channel = dto.channel,
    userId = dto.userId,
    userName = dto.userName,
    modifiedDateTime = nullableLocalDateTime(dto.modifiedDate, dto.modifiedTime),
  )
}
