package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmOrderDetailsDTO
import java.time.LocalDateTime

data class AmOrderDetails(
  val legacySubjectId: String,
  val legacyOrderId: String?,
  val firstName: String?,
  val lastName: String?,
  val alias: String?,
  val dateOfBirth: LocalDateTime?,
  val sex: String?,
  val specialInstructions: String?,
  val phoneNumber: String?,
  val address1: String?,
  val address2: String?,
  val address3: String?,
  val postcode: String?,
  val orderStartDate: LocalDateTime?,
  val orderEndDate: LocalDateTime?,
  val enforceableCondition: String?,
  val orderType: String?,
  val orderTypeDescription: String?,
  val orderEndOutcome: String?,
  val responsibleOrganisationPhoneNumber: String?,
  val responsibleOrganisationEmail: String?,
  val tagAtSource: String?,
) {
  constructor(dto: AthenaAmOrderDetailsDTO) : this (
    legacySubjectId = dto.legacySubjectId,
    legacyOrderId = dto.legacyOrderId,
    firstName = dto.firstName,
    lastName = dto.lastName,
    alias = dto.alias,
    dateOfBirth = nullableLocalDateTime(dto.dateOfBirth),
    sex = dto.sex,
    specialInstructions = dto.specialInstructions,
    phoneNumber = dto.phoneNumber,
    address1 = dto.address1,
    address2 = dto.address2,
    address3 = dto.address3,
    postcode = dto.postcode,
    orderStartDate = nullableLocalDateTime(dto.orderStartDate),
    orderEndDate = nullableLocalDateTime(dto.orderEndDate),
    orderType = dto.orderType,
    orderTypeDescription = dto.orderTypeDescription,
    enforceableCondition = dto.enforceableCondition,
    orderEndOutcome = dto.orderEndOutcome,
    responsibleOrganisationPhoneNumber = dto.responsibleOrganisationPhoneNumber,
    responsibleOrganisationEmail = dto.responsibleOrganisationEmail,
    tagAtSource = dto.tagAtSource,
  )
}
