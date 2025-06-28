package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmOrderInformationDTO
import java.time.LocalDateTime

data class AmOrderInformation(
  val legacySubjectId: String,
  val legacyOrderId: String?,
  val firstName: String?,
  val lastName: String?,
  val alias: String?,
  val dateOfBirth: LocalDateTime?,
  val address1: String?,
  val address2: String?,
  val address3: String?,
  val postcode: String?,
  val orderStartDate: LocalDateTime?,
  val orderEndDate: LocalDateTime?,
) {
  constructor(dto: AthenaAmOrderInformationDTO) : this (
    legacySubjectId = dto.legacySubjectId,
    legacyOrderId = dto.legacyOrderId,
    firstName = dto.firstName,
    lastName = dto.lastName,
    alias = dto.alias,
    dateOfBirth = nullableLocalDateTime(dto.dateOfBirth),
    address1 = dto.primaryAddressLine1,
    address2 = dto.primaryAddressLine2,
    address3 = dto.primaryAddressLine3,
    postcode = dto.primaryAddressPostCode,
    orderStartDate = nullableLocalDateTime(dto.orderStartDate),
    orderEndDate = nullableLocalDateTime(dto.orderEndDate),
  )
}
