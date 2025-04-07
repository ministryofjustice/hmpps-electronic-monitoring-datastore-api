package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSuspensionOfVisitsDTO
import java.time.LocalDateTime

data class SuspensionOfVisits(
  val legacySubjectId: String,
  val suspensionOfVisits: String?,
  val requestedDate: LocalDateTime?,
  val startDate: LocalDateTime?,
  val startTime: String?,
  val endDate: LocalDateTime?,
) {

  constructor(dto: AthenaSuspensionOfVisitsDTO) : this(
    legacySubjectId = dto.legacySubjectId.toString(),
    suspensionOfVisits = dto.suspensionOfVisits,
    requestedDate = nullableLocalDateTime(dto.suspensionOfVisitsRequestedDate),
    startDate = nullableLocalDateTime(dto.suspensionOfVisitsStartDate),
    startTime = dto.suspensionOfVisitsStartTime,
    endDate = nullableLocalDateTime(dto.suspensionOfVisitsEndDate),
  )
}
