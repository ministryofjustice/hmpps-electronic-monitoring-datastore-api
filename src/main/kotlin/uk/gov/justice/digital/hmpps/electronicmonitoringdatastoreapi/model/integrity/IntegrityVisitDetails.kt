package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityVisitDetailsDTO
import java.time.LocalDateTime

data class IntegrityVisitDetailsAddress(
  val addressLine1: String?,
  val addressLine2: String?,
  val addressLine3: String?,
  val addressLine4: String?,
  val postcode: String?,
)

data class IntegrityVisitDetails(
  val legacySubjectId: String,
  val address: IntegrityVisitDetailsAddress?,
  val actualWorkStartDateTime: LocalDateTime,
  val actualWorkEndDateTime: LocalDateTime?,
  val visitNotes: String?,
  val visitType: String?,
  val visitOutcome: String?,
) {

  constructor(dto: AthenaIntegrityVisitDetailsDTO) : this(
    legacySubjectId = dto.legacySubjectId,
    address = if (
      !dto.address1.isNullOrEmpty() ||
      !dto.address2.isNullOrEmpty() ||
      !dto.address3.isNullOrEmpty() ||
      !dto.postcode.isNullOrEmpty()
    ) {
      IntegrityVisitDetailsAddress(
        addressLine1 = dto.address1,
        addressLine2 = dto.address2,
        addressLine3 = dto.address3,
        addressLine4 = null,
        postcode = dto.postcode,
      )
    } else {
      null
    },
    actualWorkStartDateTime = LocalDateTime.parse("${dto.actualWorkStartDate}T${dto.actualWorkStartTime}"),
    actualWorkEndDateTime = nullableLocalDateTime(dto.actualWorkEndDate, dto.actualWorkEndTime),
    visitNotes = dto.visitNotes,
    visitType = dto.visitType,
    visitOutcome = dto.visitOutcome,
  )
}
