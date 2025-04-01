package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmVisitDetailsDTO
import java.time.LocalDateTime

data class AmVisitDetails(
  val legacySubjectId: String,
  val legacyOrderId: String,
  val visitId: String?,
  val visitType: String?,
  val visitAttempt: String?,
  val dateVisitRaised: LocalDateTime?,
  val visitAddress: String?,
  val visitNotes: String?,
  val visitOutcome: String?,
  val actualWorkStartDateTime: LocalDateTime?,
  val actualWorkEndDateTime: LocalDateTime?,
  val visitRejectionReason: String?,
  val visitRejectionDescription: String?,
  val visitCancelReason: String?,
  val visitCancelDescription: String?,
) {
  companion object {
    fun nullableLocalDateTime(date: String?, time: String? = "00:00:00"): LocalDateTime? = if (!date.isNullOrBlank()) {
      LocalDateTime.parse("${date}T$time")
    } else {
      null
    }
  }

  constructor(dto: AthenaAmVisitDetailsDTO) : this(
    legacySubjectId = dto.legacySubjectId,
    legacyOrderId = dto.legacyOrderId,
    visitId = dto.visitId,
    visitType = dto.visitType,
    visitAttempt = dto.visitAttempt,
    dateVisitRaised = nullableLocalDateTime(dto.dateVisitRaised),
    visitAddress = dto.visitAddress,
    visitNotes = dto.visitNotes,
    visitOutcome = dto.visitOutcome,
    actualWorkStartDateTime = nullableLocalDateTime(dto.actualWorkStartDate, dto.actualWorkStartTime),
    actualWorkEndDateTime = nullableLocalDateTime(dto.actualWorkEndDate, dto.actualWorkEndTime),
    visitRejectionReason = dto.visitRejectionReason,
    visitRejectionDescription = dto.visitRejectionDescription,
    visitCancelReason = dto.visitCancelReason,
    visitCancelDescription = dto.visitCancelDescription,
  )
}
