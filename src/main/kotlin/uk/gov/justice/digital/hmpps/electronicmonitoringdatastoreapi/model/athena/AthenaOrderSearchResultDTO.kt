package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

data class AthenaOrderSearchResultDTO(
  val legacySubjectId: String,
  val firstName: String? = "",
  val lastName: String? = "",
  val alias: String? = "",
  val primaryAddressLine1: String? = "",
  val primaryAddressLine2: String? = "",
  val primaryAddressLine3: String? = "",
  val primaryAddressPostCode: String? = "",
  val dateOfBirth: String? = "",
  val orderStartDate: String? = "",
  val orderEndDate: String? = "",
)
