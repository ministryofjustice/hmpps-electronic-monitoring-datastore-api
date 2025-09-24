package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityVisitDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import java.time.LocalDateTime

data class IntegrityVisitDetails(
  val legacySubjectId: String,
  val address: Address?,
  val actualWorkStartDateTime: LocalDateTime,
  val actualWorkEndDateTime: LocalDateTime?,
  val visitNotes: String?,
  val visitType: String?,
  val visitOutcome: String?,
) {
  data class Address(
    val addressLine1: String?,
    val addressLine2: String?,
    val addressLine3: String?,
    val addressLine4: String?,
    val postcode: String?,
  )

  constructor(dto: AthenaIntegrityVisitDetailsDTO) : this(
    legacySubjectId = dto.legacySubjectId,
    address = if (
      !dto.address1.isNullOrEmpty() ||
      !dto.address2.isNullOrEmpty() ||
      !dto.address3.isNullOrEmpty() ||
      !dto.postcode.isNullOrEmpty()
    ) {
      Address(
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
