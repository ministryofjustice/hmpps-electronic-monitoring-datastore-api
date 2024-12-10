package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Order(
  val dataType: String,
  val legacySubjectId: Long,
  val name: String,
  val address: String,
  val alias: String?,
  val dateOfBirth: String,
  val orderStartDate: String,
  val orderEndDate: String,
) {
  constructor(dto: AthenaOrderDTO) : this(
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

data class AthenaOrderDTO(
  val legacySubjectId: Long,
  val fullName: String,
  @JsonProperty("primary_address_line_1")
  val primaryAddressLine1: String,
  @JsonProperty("primary_address_line_2")
  val primaryAddressLine2: String,
  @JsonProperty("primary_address_line_3")
  val primaryAddressLine3: String,
  val primaryAddressPostCode: String,
  val orderStartDate: String,
  val orderEndDate: String,
)
