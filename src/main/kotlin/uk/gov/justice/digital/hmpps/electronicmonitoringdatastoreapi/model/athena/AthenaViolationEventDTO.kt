package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

data class AthenaViolationEventDTO(
  val legacySubjectId: Int,
  val legacyOrderId: Int,
  val enforcementReason: String,
  val investigationOutcomeReason: String,
  val breachDetails: String,
  val breachEnforcementOutcome: String,
  val agencyAction: String,
  val breachDate: String,
  val breachTime: String,
  val breachIdentifiedDate: String,
  val breachIdentifiedTime: String,
  val authorityFirstNotifiedDate: String,
  val authorityFirstNotifiedTime: String,
  val agencyResponseDate: String,
  val breachPackRequestedDate: String,
  val breachPackSentDate: String,
  val section9Date: String,
  val hearingDate: String,
  val summonsServedDate: String,
  val subjectLetterSentDate: String,
  val warningLetterSentDate: String,
  val warningLetterSentTime: String,
)
