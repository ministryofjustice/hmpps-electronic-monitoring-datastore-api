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
  val firstName: String,
  val fullName: String,
  val lastName: String,
  val legacyOrderId: Long,
  val legacySubjectId: Long,
)
