package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaAlcoholMonitoringVisitDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import java.time.LocalDateTime

data class AlcoholMonitoringVisitDetails(
  val legacySubjectId: String,
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

  constructor(dto: AthenaAlcoholMonitoringVisitDetailsDTO) : this(
    legacySubjectId = dto.legacySubjectId,
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
