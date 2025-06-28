package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityOrderInformationDTO
import java.time.LocalDateTime

data class IntegrityOrderInformation(
  val specials: String,
  val legacySubjectId: String,
  val legacyOrderId: String?,
  val name: String?,
  val alias: String?,
  val dateOfBirth: LocalDateTime?,
  val address1: String?,
  val address2: String?,
  val address3: String?,
  val postcode: String?,
  val orderStartDate: LocalDateTime?,
  val orderEndDate: LocalDateTime?,
) {
  constructor(dto: AthenaIntegrityOrderInformationDTO) : this (
    specials = "no",
    legacySubjectId = dto.legacySubjectId,
    legacyOrderId = dto.legacyOrderId,
    name = dto.name,
    alias = dto.alias,
    dateOfBirth = nullableLocalDateTime(dto.dateOfBirth),
    address1 = dto.address1,
    address2 = dto.address2,
    address3 = dto.address3,
    postcode = dto.postcode,
    orderStartDate = nullableLocalDateTime(dto.orderStartDate),
    orderEndDate = nullableLocalDateTime(dto.orderEndDate),
  )
}
