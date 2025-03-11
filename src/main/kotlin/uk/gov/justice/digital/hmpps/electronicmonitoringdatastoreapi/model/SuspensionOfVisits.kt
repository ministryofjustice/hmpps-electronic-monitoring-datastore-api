package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.processDate
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSuspensionOfVisitsDTO
import java.time.LocalDateTime

data class SuspensionOfVisits(
  val legacySubjectId: Int,
  val suspensionOfVisits: String?,
  val requestedDate: LocalDateTime?,
  val startDate: LocalDateTime?,
  val startTime: String?,
  val endDate: LocalDateTime?,
) {

  constructor(dto: AthenaSuspensionOfVisitsDTO) : this(
    legacySubjectId = dto.legacySubjectId,
    suspensionOfVisits = dto.suspensionOfVisits,
    requestedDate = processDate(dto.suspensionOfVisitsRequestedDate),
    startDate = processDate(dto.suspensionOfVisitsStartDate),
    startTime = dto.suspensionOfVisitsStartTime,
    endDate = processDate(dto.suspensionOfVisitsEndDate),
  )
}
