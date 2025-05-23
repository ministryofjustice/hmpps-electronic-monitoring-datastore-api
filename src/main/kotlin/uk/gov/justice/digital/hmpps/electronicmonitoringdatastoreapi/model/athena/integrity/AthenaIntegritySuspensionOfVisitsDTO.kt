package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity

data class AthenaIntegritySuspensionOfVisitsDTO(
  val legacySubjectId: String,
  val suspensionOfVisits: String?,
  val suspensionOfVisitsRequestedDate: String?,
  val suspensionOfVisitsStartDate: String?,
  val suspensionOfVisitsStartTime: String? = "00:00:00",
  val suspensionOfVisitsEndDate: String?,
)
