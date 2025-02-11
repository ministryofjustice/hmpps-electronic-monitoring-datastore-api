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
  override val primaryAddressLine1: String? = "",
  override val primaryAddressLine2: String? = "",
  override val primaryAddressLine3: String? = "",
  override val orderStartDate: String? = "",
  override val orderEndDate: String? = "",
  override val primaryAddressPostCode: String? = "",
  open val orderTypeDescription: String? = "",
  open val orderType: String? = "",
  open val phoneOrMobileNumber: String? = "",
  open val sex: String? = "",
): IKeyOrderInformation {
  override val name = "$firstName $lastName"

  constructor(dto: AthenaKeyOrderInformationDTO) : this (
    type = "BASE",
    specials = "no",
    legacySubjectId = dto.legacySubjectId,
    legacyOrderId = dto.legacyOrderId,
    firstName = "", //TO FIX
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
