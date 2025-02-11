package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

interface KeyOrderInformationInterface {
  val specials: String
  val legacySubjectId: String
  val legacyOrderId: String
  val name: String?
  val alias: String?
  val dateOfBirth: String?
  val primaryAddressLine1: String?
  val primaryAddressLine2: String?
  val primaryAddressLine3: String?
  val primaryAddressPostCode: String?
  val orderStartDate: String?
  val orderEndDate: String?
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
