package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.EventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityContactEventDTO
import java.time.LocalDateTime

data class IntegrityContactEventDetails(
  val outcome: String?,
  val type: String?,
  val reason: String?,
  val channel: String?,
  val userId: String?,
  val userName: String?,
  val modifiedDateTime: LocalDateTime?,
) : EventDetails() {

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
