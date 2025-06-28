package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring

data class AthenaAmOrderInformationDTO(
  val legacySubjectId: String,
  val legacyOrderId: String?,
  val firstName: String?,
  val lastName: String?,
  val alias: String?,
  val dateOfBirth: String?,
  val primaryAddressLine1: String?,
  val primaryAddressLine2: String?,
  val primaryAddressLine3: String?,
  val primaryAddressPostCode: String?,
  val orderStartDate: String?,
  val orderEndDate: String?,
)
