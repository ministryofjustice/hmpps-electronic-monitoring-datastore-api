package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

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
)
