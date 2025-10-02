package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.nullableLocalDateTime
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.AmOrderDetails
import java.time.LocalDateTime

data class AlcoholMonitoringOrderDetails(
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
  constructor(dto: AmOrderDetails) : this (
    legacySubjectId = dto.legacySubjectId,
    legacyOrderId = dto.legacyOrderId,
    firstName = dto.firstName,
    lastName = dto.lastName,
    alias = dto.alias,
    dateOfBirth = nullableLocalDateTime(dto.dateOfBirth),
    sex = dto.legacyGender,
    specialInstructions = dto.specialInstructions,
    phoneNumber = dto.phoneOrMobileNumber,
    address1 = dto.primaryAddressLine1,
    address2 = dto.primaryAddressLine2,
    address3 = dto.primaryAddressLine3,
    postcode = dto.primaryAddressPostCode,
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
