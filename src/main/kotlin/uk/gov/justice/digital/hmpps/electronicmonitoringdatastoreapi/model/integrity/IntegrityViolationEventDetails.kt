package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.EventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityViolationEventDTO
import java.time.LocalDateTime

data class IntegrityViolationEventDetails(
  val enforcementReason: String?,
  val investigationOutcomeReason: String?,
  val breachDetails: String?,
  val breachEnforcementOutcome: String?,
  val agencyAction: String?,
  val breachDateTime: LocalDateTime?,
  val breachIdentifiedDateTime: LocalDateTime?,
  val authorityFirstNotifiedDateTime: LocalDateTime?,
  val agencyResponseDate: LocalDateTime?,
  val breachPackRequestedDate: LocalDateTime?,
  val breachPackSentDate: LocalDateTime?,
  val section9Date: LocalDateTime?,
  val hearingDate: LocalDateTime?,
  val summonsServedDate: LocalDateTime?,
  val subjectLetterSentDate: LocalDateTime?,
  val warningLetterSentDateTime: LocalDateTime?,
) : EventDetails() {

  constructor(dto: AthenaIntegrityViolationEventDTO) : this (
    enforcementReason = dto.enforcementReason,
    investigationOutcomeReason = dto.investigationOutcomeReason,
    breachDetails = dto.breachDetails,
    breachEnforcementOutcome = dto.breachEnforcementOutcome,
    agencyAction = dto.agencyAction,
    breachDateTime = nullableLocalDateTime(dto.breachDate, dto.breachTime),
    breachIdentifiedDateTime = nullableLocalDateTime(dto.breachIdentifiedDate, dto.breachIdentifiedTime),
    authorityFirstNotifiedDateTime = nullableLocalDateTime(dto.authorityFirstNotifiedDate, dto.authorityFirstNotifiedTime),
    agencyResponseDate = nullableLocalDateTime(dto.agencyResponseDate, null),
    breachPackRequestedDate = nullableLocalDateTime(dto.breachPackRequestedDate, null),
    breachPackSentDate = nullableLocalDateTime(dto.breachPackSentDate, null),
    section9Date = nullableLocalDateTime(dto.section9Date, null),
    hearingDate = nullableLocalDateTime(dto.hearingDate, null),
    summonsServedDate = nullableLocalDateTime(dto.summonsServedDate, null),
    subjectLetterSentDate = nullableLocalDateTime(dto.subjectLetterSentDate, null),
    warningLetterSentDateTime = nullableLocalDateTime(dto.warningLetterSentDate, dto.warningLetterSentTime),
  )
}
