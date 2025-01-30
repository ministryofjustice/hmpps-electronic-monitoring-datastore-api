package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSuspensionOfVisitsDTO
import java.time.LocalDateTime

data class SuspensionOfVisits(
  val legacySubjectId: Int,
  val suspensionOfVisits: String,
  val suspensionOfVisitsRequestedDate: LocalDateTime,
  val suspensionOfVisitsStartDate: LocalDateTime?,
  val suspensionOfVisitsEndDate: LocalDateTime,
) {
  constructor(dto: AthenaSuspensionOfVisitsDTO) : this(
    legacySubjectId = dto.legacySubjectId,
    suspensionOfVisits = dto.suspensionOfVisits,
    suspensionOfVisitsRequestedDate = LocalDateTime.parse("${dto.suspensionOfVisitsRequestedDate}T00:00:00"),
    suspensionOfVisitsStartDate = if (!dto.suspensionOfVisitsStartDate.isNullOrEmpty()) {
      LocalDateTime.parse("${dto.suspensionOfVisitsStartDate}T${dto.suspensionOfVisitsStartTime}")
    } else {
      null
    },
    suspensionOfVisitsEndDate = LocalDateTime.parse("${dto.suspensionOfVisitsEndDate}T00:00:00"),
  )
}
