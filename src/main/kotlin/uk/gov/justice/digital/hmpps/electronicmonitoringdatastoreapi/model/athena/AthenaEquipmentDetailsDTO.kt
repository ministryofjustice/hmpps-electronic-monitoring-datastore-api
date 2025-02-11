package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

data class AthenaEquipmentDetailsDTO(
  val legacySubjectId: Int,
  val legacyOrderId: Int,
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
