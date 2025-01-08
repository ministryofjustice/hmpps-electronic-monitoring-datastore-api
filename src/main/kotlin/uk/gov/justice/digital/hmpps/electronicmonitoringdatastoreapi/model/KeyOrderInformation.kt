package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationDTO

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
  constructor(dto: AthenaKeyOrderInformationDTO) : this (
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
