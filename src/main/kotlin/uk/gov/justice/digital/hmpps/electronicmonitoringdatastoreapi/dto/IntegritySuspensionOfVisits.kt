package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.SuspensionOfVisits
import java.time.LocalDateTime

data class IntegritySuspensionOfVisits(
  val legacySubjectId: String,
  val suspensionOfVisits: String?,
  val requestedDate: LocalDateTime?,
  val startDate: LocalDateTime?,
  val startTime: String?,
  val endDate: LocalDateTime?,
) {

  constructor(dto: SuspensionOfVisits) : this(
    legacySubjectId = dto.legacySubjectId,
    suspensionOfVisits = dto.suspensionOfVisits,
    requestedDate = nullableLocalDateTime(dto.suspensionOfVisitsRequestedDate),
    startDate = nullableLocalDateTime(dto.suspensionOfVisitsStartDate),
    startTime = dto.suspensionOfVisitsStartTime,
    endDate = nullableLocalDateTime(dto.suspensionOfVisitsEndDate),
  )
}
