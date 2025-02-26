package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchResultDTO

data class OrderSearchResult(
  val dataType: String,
  val legacySubjectId: Long,
  val name: String,
  val addressLine1: String,
  val addressLine2: String,
  val addressLine3: String,
  val addressPostcode: String,
  val alias: String?,
  val dateOfBirth: String,
  val orderStartDate: String,
  val orderEndDate: String,
) {
  constructor(dto: AthenaOrderSearchResultDTO) : this(
    dataType = "am",
    legacySubjectId = dto.legacySubjectId,
    name = dto.fullName,
    addressLine1 = dto.primaryAddressLine1,
    addressLine2 = dto.primaryAddressLine2,
    addressLine3 = dto.primaryAddressLine3,
    addressPostcode = dto.primaryAddressPostCode,
    alias = null,
    dateOfBirth = dto.orderStartDate,
    orderStartDate = dto.orderStartDate,
    orderEndDate = dto.orderEndDate,
  )
}
