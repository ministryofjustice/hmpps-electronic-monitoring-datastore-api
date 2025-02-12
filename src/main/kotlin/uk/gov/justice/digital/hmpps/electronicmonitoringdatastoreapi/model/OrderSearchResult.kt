package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchResultDTO

data class OrderSearchResult(
  val dataType: String,
  val legacySubjectId: Long,
  val name: String,
  val address: String,
  val alias: String?,
  val dateOfBirth: String,
  val orderStartDate: String,
  val orderEndDate: String,
) {
  constructor(dto: AthenaOrderSearchResultDTO) : this(
    dataType = "am",
    legacySubjectId = dto.legacySubjectId,
    name = dto.fullName,
    address = StringBuilder()
      .append(dto.primaryAddressLine1)
      .append(dto.primaryAddressLine2)
      .append(dto.primaryAddressLine3)
      .append(dto.primaryAddressPostCode)
      .toString(),
    alias = null,
    dateOfBirth = dto.orderStartDate,
    orderStartDate = dto.orderStartDate,
    orderEndDate = dto.orderEndDate,
  )
}

data class OrderSearchResults(
  val results: List<OrderSearchResult>,
  val queryExecutionId: String,
)
