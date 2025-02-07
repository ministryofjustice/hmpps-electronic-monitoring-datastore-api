package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaVisitDetailsDTO
import java.time.LocalDateTime

data class VisitDetailsAddress(
  val addressLine1: String,
  val addressLine2: String,
  val addressLine3: String,
  val addressLine4: String?,
  val postcode: String,
)

data class VisitDetails(
  val legacySubjectId: Int,
  val legacyOrderId: Int,
  val address: VisitDetailsAddress,
  val actualWorkStartDateTime: LocalDateTime,
  val actualWorkEndDateTime: LocalDateTime,
  val visitNotes: String,
  val visitType: String,
  val visitOutcome: String,
) {
  constructor(dto: AthenaVisitDetailsDTO) : this(
    legacySubjectId = dto.legacySubjectId,
    legacyOrderId = dto.legacyOrderId,
    address = VisitDetailsAddress(
      addressLine1 = dto.address1,
      addressLine2 = dto.address2,
      addressLine3 = dto.address3,
      addressLine4 = null,
      postcode = dto.postcode,
    ),
    actualWorkStartDateTime = LocalDateTime.parse("${dto.actualWorkStartDate}T${dto.actualWorkStartTime}"),
    actualWorkEndDateTime = LocalDateTime.parse("${dto.actualWorkEndDate}T${dto.actualWorkEndTime}"),
    visitNotes = dto.visitNotes,
    visitType = dto.visitType,
    visitOutcome = dto.visitOutcome,
  )
}
