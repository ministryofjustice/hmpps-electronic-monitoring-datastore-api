package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring

data class AthenaAmContactEventDTO(
  val legacySubjectId: String,
  val contactDate: String?,
  val contactTime: String?,
  val inboundOrOutbound: String?,
  val fromTo: String?,
  val channel: String?,
  val subjectConsentWithdrawn: String?,
  val callOutcome: String?,
  val statement: String?,
  val reasonForContact: String?,
  val outcomeOfContact: String?,
  val visitRequired: String?,
  val visitId: String?,
)
