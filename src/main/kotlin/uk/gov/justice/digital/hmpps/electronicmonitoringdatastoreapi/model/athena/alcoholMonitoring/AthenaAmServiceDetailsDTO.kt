package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring

data class AthenaAmServiceDetailsDTO(
  val legacySubjectId: String,
  val serviceStartDate: String?,
  val serviceEndDate: String?,
  val serviceAddress: String?,
  val equipmentStartDate: String?,
  val equipmentEndDate: String?,
  val hmuSerialNumber: String?,
  val deviceSerialNumber: String?,
)
