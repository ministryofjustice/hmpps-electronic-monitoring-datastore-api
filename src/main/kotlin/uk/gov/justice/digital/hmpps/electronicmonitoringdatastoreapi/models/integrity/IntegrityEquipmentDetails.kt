package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.integrity

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.integrity.AthenaIntegrityEquipmentDetailsDTO
import java.time.LocalDateTime

data class IntegrityEquipmentDetail(
  val id: String,
  val equipmentCategoryDescription: String?,
  val installedDateTime: LocalDateTime?,
  val removedDateTime: LocalDateTime?,
)

data class IntegrityEquipmentDetails(
  val legacySubjectId: String,
  val pid: IntegrityEquipmentDetail?,
  val hmu: IntegrityEquipmentDetail?,
) {
  constructor(dto: AthenaIntegrityEquipmentDetailsDTO) : this(
    legacySubjectId = dto.legacySubjectId,
    pid = if (!dto.pidId.isNullOrEmpty()) {
      IntegrityEquipmentDetail(
        id = dto.pidId,
        equipmentCategoryDescription = dto.pidEquipmentCategoryDescription,
        installedDateTime = nullableLocalDateTime(dto.dateDeviceInstalled, dto.timeDeviceInstalled),
        removedDateTime = nullableLocalDateTime(dto.dateDeviceRemoved, dto.timeDeviceRemoved),
      )
    } else {
      null
    },
    hmu = if (!dto.hmuId.isNullOrEmpty()) {
      IntegrityEquipmentDetail(
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
