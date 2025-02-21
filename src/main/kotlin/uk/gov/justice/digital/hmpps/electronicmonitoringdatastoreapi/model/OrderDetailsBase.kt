package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

open class OrderDetailsBase(
  open val type: String,
  open val specials: String,
  open val legacySubjectId: String,
  open val legacyOrderId: String,
  open val firstName: String? = "",
  open val lastName: String? = "",
  open val alias: String? = "",
  open val dateOfBirth: String? = "",
  open val primaryAddressLine1: String? = "",
  open val primaryAddressLine2: String? = "",
  open val primaryAddressLine3: String? = "",
  open val orderStartDate: String? = "",
  open val orderEndDate: String? = "",
  open val primaryAddressPostCode: String? = "",
  open val orderTypeDescription: String? = "",
  open val orderType: String? = "",
  open val phoneOrMobileNumber: String? = "",
  open val sex: String? = "",
)
