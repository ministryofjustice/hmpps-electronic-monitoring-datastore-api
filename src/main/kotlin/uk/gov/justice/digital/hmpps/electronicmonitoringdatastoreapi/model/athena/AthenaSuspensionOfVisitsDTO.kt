package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

data class AthenaSuspensionOfVisitsDTO(
  val legacySubjectId: Int,
  val suspensionOfVisits: String,
  val suspensionOfVisitsRequestedDate: String,
  val suspensionOfVisitsStartDate: String,
  val suspensionOfVisitsStartTime: String,
  val suspensionOfVisitsEndDate: String,
)
