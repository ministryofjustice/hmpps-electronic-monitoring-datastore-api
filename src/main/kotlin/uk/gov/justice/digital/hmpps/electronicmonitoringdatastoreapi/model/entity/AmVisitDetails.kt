package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity

data class AmVisitDetails(
  val legacySubjectId: String,
  val visitId: String?,
  val visitType: String?,
  val visitAttempt: String?,
  val dateVisitRaised: String?,
  val visitAddress: String?,
  val visitNotes: String?,
  val visitOutcome: String?,
  val actualWorkStartDate: String?,
  val actualWorkStartTime: String?,
  val actualWorkEndDate: String?,
  val actualWorkEndTime: String?,
  val visitRejectionReason: String?,
  val visitRejectionDescription: String?,
  val visitCancelReason: String?,
  val visitCancelDescription: String?,
)
