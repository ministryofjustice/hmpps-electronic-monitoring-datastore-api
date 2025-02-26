package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.processDate
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationDTO
import java.time.LocalDateTime

data class KeyOrderInformation(
  val specials: String,
  val legacySubjectId: String,
  val legacyOrderId: String,
  val name: String,
  val alias: String?,
  val dateOfBirth: LocalDateTime?,
  val address1: String,
  val address2: String,
  val address3: String,
  val postcode: String,
  val orderStartDate: LocalDateTime?,
  val orderEndDate: LocalDateTime?,
) {

  constructor(dto: AthenaKeyOrderInformationDTO) : this (
    specials = "no",
    legacySubjectId = dto.legacySubjectId,
    legacyOrderId = dto.legacyOrderId,
    name = dto.name,
    alias = dto.alias,
    dateOfBirth = processDate(dto.orderStartDate),
    address1 = dto.address1,
    address2 = dto.address2,
    address3 = dto.address3,
    postcode = dto.postcode,
    orderStartDate = processDate(dto.orderStartDate),
    orderEndDate = processDate(dto.orderEndDate),
  )
}
