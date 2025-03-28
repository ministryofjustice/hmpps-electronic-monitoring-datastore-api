package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.processDate
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchResultDTO
import java.time.LocalDateTime

data class OrderSearchResult(
  val dataType: String,
  val legacySubjectId: String,
  val firstName: String?,
  val lastName: String?,
  val alias: String?,
  val addressLine1: String?,
  val addressLine2: String?,
  val addressLine3: String?,
  val addressPostcode: String?,
  val dateOfBirth: LocalDateTime?,
  val orderStartDate: LocalDateTime?,
  val orderEndDate: LocalDateTime?,
) {
  constructor(dto: AthenaOrderSearchResultDTO) : this(
    dataType = "am",
    legacySubjectId = dto.legacySubjectId,
    firstName = dto.firstName,
    lastName = dto.lastName,
    alias = dto.alias,
    addressLine1 = dto.primaryAddressLine1,
    addressLine2 = dto.primaryAddressLine2,
    addressLine3 = dto.primaryAddressLine3,
    addressPostcode = dto.primaryAddressPostCode,
    dateOfBirth = processDate(dto.dateOfBirth),
    orderStartDate = processDate(dto.orderStartDate),
    orderEndDate = processDate(dto.orderEndDate),
  )
}
