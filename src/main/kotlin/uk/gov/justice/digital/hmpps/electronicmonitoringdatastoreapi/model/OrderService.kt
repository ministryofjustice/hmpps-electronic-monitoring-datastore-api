package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaServicesDTO

data class OrderService(
  val legacySubjectId: Int,
  val serviceId: Int,
  val serviceAddress1: String,
  val serviceAddress2: String,
  val serviceAddress3: String,
  val serviceAddressPostcode: String,
  val serviceStartDate: String,
  val serviceEndDate: String,
  val curfewStartDate: String,
  val curfewEndDate: String,
  val curfewStartTime: String,
  val curfewEndTime: String,
  val monday: Int,
  val tuesday: Int,
  val wednesday: Int,
  val thursday: Int,
  val friday: Int,
  val saturday: Int,
  val sunday: Int,
) {
  constructor(dto: AthenaServicesDTO) : this (
    legacySubjectId = dto.legacySubjectId,
    serviceId = dto.serviceId,
    serviceAddress1 = dto.serviceAddress1,
    serviceAddress2 = dto.serviceAddress2,
    serviceAddress3 = dto.serviceAddress3,
    serviceAddressPostcode = dto.serviceAddressPostcode,
    serviceStartDate = dto.serviceStartDate,
    serviceEndDate = dto.serviceEndDate,
    curfewStartDate = dto.curfewStartDate,
    curfewEndDate = dto.curfewEndDate,
    curfewStartTime = dto.curfewStartTime,
    curfewEndTime = dto.curfewEndTime,
    monday = dto.monday,
    tuesday = dto.tuesday,
    wednesday = dto.wednesday,
    thursday = dto.thursday,
    friday = dto.friday,
    saturday = dto.saturday,
    sunday = dto.sunday,
  )
}
