package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

data class Order(
  val dataType: String,
  val legacySubjectId: Long,
  val name: String,
  val address: String,
  val alias: String?,
  val dateOfBirth: String,
  val orderStartDate: String,
  val orderEndDate: String,
)

data class MiniOrder(
  val legacySubjectId: Long,
  val legacyOrderId: Long,
  val firstName: String,
  val lastName: String,
  val fullName: String,
)
