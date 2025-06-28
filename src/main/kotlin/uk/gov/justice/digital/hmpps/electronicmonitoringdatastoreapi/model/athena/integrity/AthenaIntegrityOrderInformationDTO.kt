package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity

data class AthenaIntegrityOrderInformationDTO(
  val legacySubjectId: String,
  val legacyOrderId: String?,
  val fullName: String?,
  val alias: String?,
  val dateOfBirth: String?,
  val primaryAddressLine1: String?,
  val primaryAddressLine2: String?,
  val primaryAddressLine3: String?,
  val primaryAddressPostCode: String?,
  val orderStartDate: String?,
  val orderEndDate: String?,
)
