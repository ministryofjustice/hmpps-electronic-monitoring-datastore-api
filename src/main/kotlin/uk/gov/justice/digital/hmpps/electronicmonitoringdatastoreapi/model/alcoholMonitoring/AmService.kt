package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmServiceDTO
import java.time.LocalDateTime

data class AmService(
  val legacySubjectId: String,
  val legacyOrderId: String,
  val serviceStartDate: LocalDateTime?,
  val serviceEndDate: LocalDateTime?,
  val serviceAddress: String?,
  val equipmentStartDate: LocalDateTime?,
  val equipmentEndDate: LocalDateTime?,
  val hmuSerialNumber: String?,
  val deviceSerialNumber: String?,
) {

  constructor(dto: AthenaAmServiceDTO) : this(
    legacySubjectId = dto.legacySubjectId,
    legacyOrderId = dto.legacyOrderId,
    serviceStartDate = nullableLocalDateTime(dto.serviceStartDate),
    serviceEndDate = nullableLocalDateTime(dto.serviceEndDate),
    serviceAddress = dto.serviceAddress,
    equipmentStartDate = nullableLocalDateTime(dto.equipmentStartDate),
    equipmentEndDate = nullableLocalDateTime(dto.equipmentEndDate),
    hmuSerialNumber = dto.hmuSerialNumber,
    deviceSerialNumber = dto.deviceSerialNumber,
  )
}
