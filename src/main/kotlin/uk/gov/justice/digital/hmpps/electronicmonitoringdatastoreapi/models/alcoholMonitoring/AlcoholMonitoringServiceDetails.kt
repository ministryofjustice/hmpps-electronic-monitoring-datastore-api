package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.alcoholMonitoring.AthenaAlcoholMonitoringServiceDetailsDTO
import java.time.LocalDateTime

data class AlcoholMonitoringServiceDetails(
  val legacySubjectId: String,
  val serviceStartDate: LocalDateTime?,
  val serviceEndDate: LocalDateTime?,
  val serviceAddress: String?,
  val equipmentStartDate: LocalDateTime?,
  val equipmentEndDate: LocalDateTime?,
  val hmuSerialNumber: String?,
  val deviceSerialNumber: String?,
) {

  constructor(dto: AthenaAlcoholMonitoringServiceDetailsDTO) : this(
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
