package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import com.fasterxml.jackson.annotation.JsonProperty

data class KeyOrderInformation(
  val specials: String,
  val legacySubjectId: String,
  val legacyOrderId: String,
  val name: String,
  val alias: String?,
  val dateOfBirth: String,
  val address1: String,
  val address2: String,
  val address3: String,
  val postcode: String,
  val orderStartDate: String,
  val orderEndDate: String,
) {
  constructor(dto: AthenaKeyOrderDTO) : this (
    specials = "no",
    legacySubjectId = dto.legacySubjectId,
    legacyOrderId = dto.legacyOrderId,
    name = dto.name,
    alias = dto.alias,
    dateOfBirth = dto.dateOfBirth,
    address1 = dto.address1,
    address2 = dto.address2,
    address3 = dto.address3,
    postcode = dto.postcode,
    orderStartDate = dto.orderStartDate,
    orderEndDate = dto.orderEndDate,
  )
}

data class AthenaKeyOrderDTO(
  val legacySubjectId: String,
  val legacyOrderId: String,
  @JsonProperty("full_name")
  val name: String,
  val alias: String?,
  val dateOfBirth: String,
  @JsonProperty("primary_address_line_1")
  val address1: String,
  @JsonProperty("primary_address_line_2")
  val address2: String,
  @JsonProperty("primary_address_line_3")
  val address3: String,
  @JsonProperty("primary_address_post_code")
  val postcode: String,
  val orderStartDate: String,
  val orderEndDate: String,
)

// legacy_subject_id
//              , legacy_order_id
//              , full_name
//              , alias
//              , date_of_birth
//              , primary_address_line_1
//              , primary_address_line_2
//              , primary_address_line_3
//              , primary_address_post_code
//              , order_start_date
//              , order_end_date
