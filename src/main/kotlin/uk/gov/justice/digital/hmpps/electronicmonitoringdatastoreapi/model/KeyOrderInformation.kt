package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

class KeyOrderInformation(
  orderDetails: OrderDetailsBase,
) {
  val specials: String = orderDetails.specials
  val legacySubjectId: String = orderDetails.legacySubjectId
  val legacyOrderId: String = orderDetails.legacyOrderId
  val name: String = "${orderDetails.firstName} ${orderDetails.lastName}"
  val alias: String? = orderDetails.alias
  val dateOfBirth: String? = orderDetails.dateOfBirth
  val primaryAddressLine1: String? = orderDetails.primaryAddressLine1
  val primaryAddressLine2: String? = orderDetails.primaryAddressLine2
  val primaryAddressLine3: String? = orderDetails.primaryAddressLine3
  val primaryAddressPostCode: String? = orderDetails.primaryAddressPostCode
  val orderStartDate: String? = orderDetails.orderStartDate
  val orderEndDate: String? = orderDetails.orderEndDate
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
