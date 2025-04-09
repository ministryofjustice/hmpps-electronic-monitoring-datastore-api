package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

data class AthenaVisitDetailsDTO(
  val legacySubjectId: String,
  val legacyOrderId: String,
  val address1: String?,
  val address2: String?,
  val address3: String?,
  val postcode: String?,
  val actualWorkStartDate: String?,
  val actualWorkStartTime: String?,
  val actualWorkEndDate: String?,
  val actualWorkEndTime: String?,
  val visitNotes: String?,
  val visitType: String?,
  val visitOutcome: String?,
)
