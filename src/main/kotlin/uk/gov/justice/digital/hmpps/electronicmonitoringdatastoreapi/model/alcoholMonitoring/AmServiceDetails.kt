package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmServiceDetailsDTO
import java.time.LocalDateTime

data class AmServiceDetails(
  val legacySubjectId: String,
  val serviceStartDate: LocalDateTime?,
  val serviceEndDate: LocalDateTime?,
  val serviceAddress: String?,
  val equipmentStartDate: LocalDateTime?,
  val equipmentEndDate: LocalDateTime?,
  val hmuSerialNumber: String?,
  val deviceSerialNumber: String?,
) {

  constructor(dto: AthenaAmServiceDetailsDTO) : this(
    legacySubjectId = dto.legacySubjectId,
    serviceStartDate = nullableLocalDateTime(dto.serviceStartDate),
    serviceEndDate = nullableLocalDateTime(dto.serviceEndDate),
    serviceAddress = dto.serviceAddress,
    equipmentStartDate = nullableLocalDateTime(dto.equipmentStartDate),
    equipmentEndDate = nullableLocalDateTime(dto.equipmentEndDate),
    hmuSerialNumber = dto.hmuSerialNumber,
    deviceSerialNumber = dto.deviceSerialNumber,
  )
}
