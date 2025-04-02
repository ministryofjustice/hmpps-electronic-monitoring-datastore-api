package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaEquipmentDetailsDTO
import java.time.LocalDateTime

data class EquipmentDetail(
  val id: String,
  val equipmentCategoryDescription: String?,
  val installedDateTime: LocalDateTime?,
  val removedDateTime: LocalDateTime?,
)

data class EquipmentDetails(
  val legacySubjectId: Int,
  val legacyOrderId: Int,
  val pid: EquipmentDetail?,
  val hmu: EquipmentDetail?,
) {
  constructor(dto: AthenaEquipmentDetailsDTO) : this(
    legacySubjectId = dto.legacySubjectId,
    legacyOrderId = dto.legacyOrderId,
    pid = if (!dto.pidId.isNullOrEmpty()) {
      EquipmentDetail(
        id = dto.pidId,
        equipmentCategoryDescription = dto.pidEquipmentCategoryDescription,
        installedDateTime = nullableLocalDateTime(dto.dateDeviceInstalled, dto.timeDeviceInstalled),
        removedDateTime = nullableLocalDateTime(dto.dateDeviceRemoved, dto.timeDeviceRemoved),
      )
    } else {
      null
    },
    hmu = if (!dto.hmuId.isNullOrEmpty()) {
      EquipmentDetail(
        id = dto.hmuId,
        equipmentCategoryDescription = dto.hmuEquipmentCategoryDescription,
        installedDateTime = nullableLocalDateTime(dto.hmuInstallDate, dto.hmuInstallTime),
        removedDateTime = nullableLocalDateTime(dto.hmuRemovedDate, dto.hmuRemovedTime),
      )
    } else {
      null
    },
  )
}
