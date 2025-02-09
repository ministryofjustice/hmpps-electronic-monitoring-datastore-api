package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaViolationEventDTO
import java.time.LocalDateTime

data class ViolationEventDetails(
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
  companion object {
    private fun asNullableDateTime(date: String?, time: String?): LocalDateTime? = if (date != null) {
      LocalDateTime.parse("${date}T${time ?: "00:00:00"}")
    } else {
      null
    }
  }

  constructor(dto: AthenaViolationEventDTO) : this (
    enforcementReason = dto.enforcementReason,
    investigationOutcomeReason = dto.investigationOutcomeReason,
    breachDetails = dto.breachDetails,
    breachEnforcementOutcome = dto.breachEnforcementOutcome,
    agencyAction = dto.agencyAction,
    breachDateTime = asNullableDateTime(dto.breachDate, dto.breachTime),
    breachIdentifiedDateTime = asNullableDateTime(dto.breachIdentifiedDate, dto.breachIdentifiedTime),
    authorityFirstNotifiedDateTime = asNullableDateTime(dto.authorityFirstNotifiedDate, dto.authorityFirstNotifiedTime),
    agencyResponseDate = asNullableDateTime(dto.agencyResponseDate, null),
    breachPackRequestedDate = asNullableDateTime(dto.breachPackRequestedDate, null),
    breachPackSentDate = asNullableDateTime(dto.breachPackSentDate, null),
    section9Date = asNullableDateTime(dto.section9Date, null),
    hearingDate = asNullableDateTime(dto.hearingDate, null),
    summonsServedDate = asNullableDateTime(dto.summonsServedDate, null),
    subjectLetterSentDate = asNullableDateTime(dto.subjectLetterSentDate, null),
    warningLetterSentDateTime = asNullableDateTime(dto.warningLetterSentDate, dto.warningLetterSentTime),
  )
}
