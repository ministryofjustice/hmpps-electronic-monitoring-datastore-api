package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring

data class AthenaAmEquipmentDetailsDTO(
  val legacySubjectId: String,
  val legacyOrderId: String,
  val deviceType: String?,
  val deviceSerialNumber: String?,
  val deviceAddressType: String?,
  val legFitting: String?,
  val dateDeviceInstalled: String?,
  val timeDeviceInstalled: String?,
  val dateDeviceRemoved: String?,
  val timeDeviceRemoved: String?,
  val hmuInstallDate: String?,
  val hmuInstallTime: String?,
  val hmuUninstallDate: String?,
  val hmuUninstallTime: String?,
)
