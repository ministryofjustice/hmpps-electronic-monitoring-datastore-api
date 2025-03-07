package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaAmOrderDetailsDTO

data class AmOrderDetails(
  val specials: String,
  val legacySubjectId: String,
  val legacyOrderId: String,
  val firstName: String?,
  val lastName: String?,
  val alias: String?,
  val dateOfBirth: String?,

  val sex: String?,

  val primaryAddressLine1: String?,
  val primaryAddressLine2: String?,
  val primaryAddressLine3: String?,
  val primaryAddressPostCode: String?,
  val phoneOrMobileNumber: String?,

  // Many fields not present in AM order, only in normal orders

  val orderStartDate: String?,
  val orderEndDate: String?,
  val orderType: String?,
  val orderTypeDescription: String?,

  val enforceableCondition: String?,
  val orderEndOutcome: String?,
  val responsibleOrgDetailsPhoneNumber: String?,
  val responsibleOrgDetailsEmail: String?,
  val tagAtSource: String?,
  val specialInstructions: String?,
) {
  constructor(dto: AthenaAmOrderDetailsDTO) : this (
    specials = "no",
    legacySubjectId = dto.legacySubjectId,
    legacyOrderId = dto.legacyOrderId,
    firstName = dto.firstName,
    lastName = dto.lastName,
    alias = dto.alias,
    dateOfBirth = dto.dateOfBirth,
    sex = dto.sex,
    primaryAddressLine1 = dto.primaryAddressLine1,
    primaryAddressLine2 = dto.primaryAddressLine2,
    primaryAddressLine3 = dto.primaryAddressLine3,
    primaryAddressPostCode = dto.primaryAddressPostCode,
    phoneOrMobileNumber = dto.phoneOrMobileNumber,
    orderStartDate = dto.orderStartDate,
    orderEndDate = dto.orderEndDate,
    orderType = dto.orderType,
    orderTypeDescription = dto.orderTypeDescription,
    enforceableCondition = dto.enforceableCondition,
    orderEndOutcome = dto.orderEndOutcome,
    responsibleOrgDetailsPhoneNumber = dto.responsibleOrgDetailsPhoneNumber,
    responsibleOrgDetailsEmail = dto.responsibleOrgDetailsEmail,
    tagAtSource = dto.tagAtSource,
    specialInstructions = dto.specialInstructions,
  )
}
