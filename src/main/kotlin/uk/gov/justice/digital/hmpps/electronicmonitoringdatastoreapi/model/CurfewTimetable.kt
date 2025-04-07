package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaCurfewTimetableDTO
import java.time.LocalDateTime

data class CurfewTimetable(
  val legacySubjectId: String,
  val legacyOrderId: String,
  val serviceId: Int,
  val serviceAddress1: String?,
  val serviceAddress2: String?,
  val serviceAddress3: String?,
  val serviceAddressPostcode: String?,
  val serviceStartDate: LocalDateTime?,
  val serviceEndDate: LocalDateTime?,
  val curfewStartDate: LocalDateTime?,
  val curfewEndDate: LocalDateTime?,
  val monday: Int,
  val tuesday: Int,
  val wednesday: Int,
  val thursday: Int,
  val friday: Int,
  val saturday: Int,
  val sunday: Int,
) {
  constructor(dto: AthenaCurfewTimetableDTO) : this (
    legacySubjectId = dto.legacySubjectId.toString(),
    legacyOrderId = dto.legacyOrderId.toString(),
    serviceId = dto.serviceId,
    serviceAddress1 = dto.serviceAddress1,
    serviceAddress2 = dto.serviceAddress2,
    serviceAddress3 = dto.serviceAddress3,
    serviceAddressPostcode = dto.serviceAddressPostcode,
    serviceStartDate = LocalDateTime.parse("${dto.serviceStartDate}T00:00:00"),
    serviceEndDate = LocalDateTime.parse("${dto.serviceEndDate}T00:00:00"),
    curfewStartDate = LocalDateTime.parse("${dto.curfewStartDate}T${dto.curfewStartTime}"),
    curfewEndDate = LocalDateTime.parse("${dto.curfewEndDate}T${dto.curfewEndTime}"),
    monday = dto.monday,
    tuesday = dto.tuesday,
    wednesday = dto.wednesday,
    thursday = dto.thursday,
    friday = dto.friday,
    saturday = dto.saturday,
    sunday = dto.sunday,
  )
}
