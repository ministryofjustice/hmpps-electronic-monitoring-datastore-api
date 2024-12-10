package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers

data class MiniOrder(
  val firstName: String,
  val fullName: String,
  val lastName: String,
  val legacyOrderId: Long,
  val legacySubjectId: Long,
)
