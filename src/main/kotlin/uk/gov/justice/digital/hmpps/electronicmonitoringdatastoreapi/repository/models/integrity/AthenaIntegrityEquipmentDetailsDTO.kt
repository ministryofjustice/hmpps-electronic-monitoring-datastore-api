package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.models.integrity

data class AthenaIntegrityEquipmentDetailsDTO(
  val legacySubjectId: String,
  val hmuId: String?,
  val hmuEquipmentCategoryDescription: String?,
  val hmuInstallDate: String?,
  val hmuInstallTime: String?,
  val hmuRemovedDate: String?,
  val hmuRemovedTime: String?,
  val pidId: String?,
  val pidEquipmentCategoryDescription: String?,
  val dateDeviceInstalled: String?,
  val timeDeviceInstalled: String?,
  val dateDeviceRemoved: String?,
  val timeDeviceRemoved: String?,
)
