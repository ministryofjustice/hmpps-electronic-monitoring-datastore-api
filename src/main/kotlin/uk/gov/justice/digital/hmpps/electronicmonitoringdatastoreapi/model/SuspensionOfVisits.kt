package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.processDate
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSuspensionOfVisitsDTO
import java.time.LocalDateTime

data class SuspensionOfVisits(
  val legacySubjectId: Int,
  val suspensionOfVisits: String,
  val suspensionOfVisitsRequestedDate: LocalDateTime?,
  val suspensionOfVisitsStartDate: LocalDateTime?,
  val suspensionOfVisitsStartTime: String?,
  val suspensionOfVisitsEndDate: LocalDateTime?,
) {

  constructor(dto: AthenaSuspensionOfVisitsDTO) : this(
    legacySubjectId = dto.legacySubjectId,
    suspensionOfVisits = dto.suspensionOfVisits,
    suspensionOfVisitsRequestedDate = processDate(dto.suspensionOfVisitsRequestedDate),
    suspensionOfVisitsStartDate = processDate(dto.suspensionOfVisitsStartDate),
    suspensionOfVisitsStartTime = dto.suspensionOfVisitsStartTime,
    suspensionOfVisitsEndDate = processDate(dto.suspensionOfVisitsEndDate),
  )
}
