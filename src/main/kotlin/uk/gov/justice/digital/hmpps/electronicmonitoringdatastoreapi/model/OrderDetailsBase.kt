package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationDTO

open class OrderDetailsBase(
  val type: String,
  override val specials: String,
  override val legacySubjectId: String,
  override val legacyOrderId: String,
  open val firstName: String? = "",
  open val lastName: String? = "",
  override val alias: String? = "",
  override val dateOfBirth: String? = "",
  open val primaryAddressLine1: String? = "",
  open val primaryAddressLine2: String? = "",
  open val primaryAddressLine3: String? = "",
  override val orderStartDate: String? = "",
  override val orderEndDate: String? = "",
  open val primaryAddressPostCode: String? = "",
  open val orderTypeDescription: String? = "",
  open val orderType: String? = "",
  open val phoneOrMobileNumber: String? = "",
  open val sex: String? = "",
): IKeyOrderInformation {
  override val name = "$firstName $lastName"
  override val address1 = primaryAddressLine1
  override val address2 = primaryAddressLine2
  override val address3 = primaryAddressLine3
  override val postcode = primaryAddressPostCode

  constructor(dto: AthenaKeyOrderInformationDTO) : this (
    type = "BASE",
    specials = "no",
    legacySubjectId = dto.legacySubjectId,
    legacyOrderId = dto.legacyOrderId,
    firstName = dto.name, //TO FIX
    lastName = dto.name, //TO FIX
    alias = dto.alias,
    dateOfBirth = dto.dateOfBirth,
    primaryAddressLine1 = dto.address1,
    primaryAddressLine2 = dto.address2,
    primaryAddressLine3 = dto.address3,
    orderStartDate = dto.orderStartDate,
    orderEndDate = dto.orderEndDate,
    primaryAddressPostCode = dto.postcode
  )
}
