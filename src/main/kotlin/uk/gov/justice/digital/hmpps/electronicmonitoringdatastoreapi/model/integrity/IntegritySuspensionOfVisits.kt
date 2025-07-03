package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.models.integrity.AthenaIntegritySuspensionOfVisitsDTO
import java.time.LocalDateTime

data class IntegritySuspensionOfVisits(
  val legacySubjectId: String,
  val suspensionOfVisits: String?,
  val requestedDate: LocalDateTime?,
  val startDate: LocalDateTime?,
  val startTime: String?,
  val endDate: LocalDateTime?,
) {

  constructor(dto: AthenaIntegritySuspensionOfVisitsDTO) : this(
    legacySubjectId = dto.legacySubjectId,
    suspensionOfVisits = dto.suspensionOfVisits,
    requestedDate = nullableLocalDateTime(dto.suspensionOfVisitsRequestedDate),
    startDate = nullableLocalDateTime(dto.suspensionOfVisitsStartDate),
    startTime = dto.suspensionOfVisitsStartTime,
    endDate = nullableLocalDateTime(dto.suspensionOfVisitsEndDate),
  )
}
