package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaViolationEventDTO
import java.time.LocalDateTime

data class ViolationEventDetails(
  val enforcementReason: String,
  val investigationOutcomeReason: String,
  val breachDetails: String,
  val breachEnforcementOutcome: String,
  val agencyAction: String,
  val breachDateTime: LocalDateTime,
  val breachIdentifiedDateTime: LocalDateTime,
  val authorityFirstNotifiedDateTime: LocalDateTime,
  val agencyResponseDate: LocalDateTime,
  val breachPackRequestedDate: LocalDateTime,
  val breachPackSentDate: LocalDateTime,
  val section9Date: LocalDateTime,
  val hearingDate: LocalDateTime,
  val summonsServedDate: LocalDateTime,
  val subjectLetterSentDate: LocalDateTime,
  val warningLetterSentDateTime: LocalDateTime,
) : EventDetails() {
  constructor(dto: AthenaViolationEventDTO) : this (
    enforcementReason = dto.enforcementReason,
    investigationOutcomeReason = dto.investigationOutcomeReason,
    breachDetails = dto.breachDetails,
    breachEnforcementOutcome = dto.breachEnforcementOutcome,
    agencyAction = dto.agencyAction,
    breachDateTime = LocalDateTime.parse("${dto.breachDate}T${dto.breachTime}"),
    breachIdentifiedDateTime = LocalDateTime.parse("${dto.breachIdentifiedDate}T${dto.breachIdentifiedTime}"),
    authorityFirstNotifiedDateTime = LocalDateTime.parse("${dto.authorityFirstNotifiedDate}T${dto.authorityFirstNotifiedTime}"),
    agencyResponseDate = LocalDateTime.parse("${dto.agencyResponseDate}T00:00:00"),
    breachPackRequestedDate = LocalDateTime.parse("${dto.breachPackRequestedDate}T00:00:00"),
    breachPackSentDate = LocalDateTime.parse("${dto.breachPackSentDate}T00:00:00"),
    section9Date = LocalDateTime.parse("${dto.section9Date}T00:00:00"),
    hearingDate = LocalDateTime.parse("${dto.hearingDate}T00:00:00"),
    summonsServedDate = LocalDateTime.parse("${dto.summonsServedDate}T00:00:00"),
    subjectLetterSentDate = LocalDateTime.parse("${dto.subjectLetterSentDate}T00:00:00"),
    warningLetterSentDateTime = LocalDateTime.parse("${dto.warningLetterSentDate}T${dto.warningLetterSentTime}"),
  )
}
