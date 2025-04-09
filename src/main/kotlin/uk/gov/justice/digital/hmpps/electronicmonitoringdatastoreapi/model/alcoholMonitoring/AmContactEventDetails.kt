package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmContactEventDTO
import java.time.LocalDateTime

data class AmContactEventDetails(
  val contactDateTime: LocalDateTime?,
  val inboundOrOutbound: String?,
  val fromTo: String?,
  val channel: String?,
  val subjectConsentWithdrawn: String?,
  val callOutcome: String?,
  val statement: String?,
  val reasonForContact: String?,
  val outcomeOfContact: String?,
  val visitRequired: String?,
  val visitId: String?,
) : AmEventDetails() {

  constructor(dto: AthenaAmContactEventDTO) : this (
    contactDateTime = nullableLocalDateTime(dto.contactDate, dto.contactTime),
    inboundOrOutbound = dto.inboundOrOutbound,
    fromTo = dto.fromTo,
    channel = dto.channel,
    subjectConsentWithdrawn = dto.subjectConsentWithdrawn,
    callOutcome = dto.callOutcome,
    statement = dto.statement,
    reasonForContact = dto.reasonForContact,
    outcomeOfContact = dto.outcomeOfContact,
    visitRequired = dto.visitRequired,
    visitId = dto.visitId,
  )
}
