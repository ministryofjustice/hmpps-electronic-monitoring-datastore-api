package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaEquipmentDetailsDTO
import java.time.LocalDateTime

data class EquipmentDetail(
  val id: String,
  val equipmentCategoryDescription: String,
  val installedDateTime: LocalDateTime,
  val removedDate: LocalDateTime,
)

data class EquipmentDetails(
  val legacySubjectId: Int,
  val legacyOrderId: Int,
  val pid: EquipmentDetail,
  val hmu: EquipmentDetail,
) {
  constructor(dto: AthenaEquipmentDetailsDTO) : this(
    legacySubjectId = dto.legacySubjectId,
    legacyOrderId = dto.legacyOrderId,
    pid = EquipmentDetail(
      id = dto.pidId,
      equipmentCategoryDescription = dto.pidEquipmentCategoryDescription,
      installedDateTime = LocalDateTime.parse("${dto.dateDeviceInstalled}T${dto.timeDeviceInstalled}"),
      removedDate = LocalDateTime.parse("${dto.dateDeviceRemoved}T${dto.timeDeviceRemoved}"),
    ),
    hmu = EquipmentDetail(
      id = dto.hmuId,
      equipmentCategoryDescription = dto.hmuEquipmentCategoryDescription,
      installedDateTime = LocalDateTime.parse("${dto.hmuInstallDate}T${dto.hmuInstallTime}"),
      removedDate = LocalDateTime.parse("${dto.hmuRemovedDate}T${dto.hmuRemovedTime}"),
    ),
  )
}
