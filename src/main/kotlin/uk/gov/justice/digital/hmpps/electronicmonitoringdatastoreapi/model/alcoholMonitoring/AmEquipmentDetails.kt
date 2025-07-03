package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.models.alcoholMonitoring.AthenaAmEquipmentDetailsDTO
import java.time.LocalDateTime

data class AmEquipmentDetails(
  val legacySubjectId: String,
  val deviceType: String?,
  val deviceSerialNumber: String?,
  val deviceAddressType: String?,
  val legFitting: String?,
  val deviceInstalledDateTime: LocalDateTime?,
  val deviceRemovedDateTime: LocalDateTime?,
  val hmuInstallDateTime: LocalDateTime?,
  val hmuRemovedDateTime: LocalDateTime?,
) {

  constructor(dto: AthenaAmEquipmentDetailsDTO) : this(
    legacySubjectId = dto.legacySubjectId,
    deviceType = dto.deviceType,
    deviceSerialNumber = dto.deviceSerialNumber,
    deviceAddressType = dto.deviceAddressType,
    legFitting = dto.legFitting,
    deviceInstalledDateTime = nullableLocalDateTime(dto.dateDeviceInstalled, dto.timeDeviceInstalled),
    deviceRemovedDateTime = nullableLocalDateTime(dto.dateDeviceRemoved, dto.timeDeviceRemoved),
    hmuInstallDateTime = nullableLocalDateTime(dto.hmuInstallDate, dto.hmuInstallTime),
    hmuRemovedDateTime = nullableLocalDateTime(dto.hmuRemovedDate, dto.hmuRemovedTime),
  )
}
