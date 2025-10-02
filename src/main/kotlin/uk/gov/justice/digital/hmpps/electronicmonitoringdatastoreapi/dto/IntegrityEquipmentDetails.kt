package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.EquipmentDetails
import java.time.LocalDateTime

data class IntegrityEquipmentDetails(
  val legacySubjectId: String,
  val pid: Equipment?,
  val hmu: Equipment?,
) {
  data class Equipment(
    val id: String,
    val equipmentCategoryDescription: String?,
    val installedDateTime: LocalDateTime?,
    val removedDateTime: LocalDateTime?,
  )

  constructor(dto: EquipmentDetails) : this(
    legacySubjectId = dto.legacySubjectId,
    pid = if (!dto.pidId.isNullOrEmpty()) {
      Equipment(
        id = dto.pidId,
        equipmentCategoryDescription = dto.pidEquipmentCategoryDescription,
        installedDateTime = nullableLocalDateTime(dto.dateDeviceInstalled, dto.timeDeviceInstalled),
        removedDateTime = nullableLocalDateTime(dto.dateDeviceRemoved, dto.timeDeviceRemoved),
      )
    } else {
      null
    },
    hmu = if (!dto.hmuId.isNullOrEmpty()) {
      Equipment(
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
