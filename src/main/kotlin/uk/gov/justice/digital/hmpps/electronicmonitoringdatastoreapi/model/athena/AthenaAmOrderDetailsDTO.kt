package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

import com.fasterxml.jackson.annotation.JsonProperty

// TODO: Check which fields are nullable
data class AthenaAmOrderDetailsDTO(
  val legacySubjectId: String,
  val legacyOrderId: String,
  val firstName: String? = "",
  val lastName: String? = "",
  val alias: String? = "",
  val dateOfBirth: String? = "",

  @JsonProperty("legacy_gender")
  val sex: String? = "",

  @JsonProperty("primary_address_line1")
  val primaryAddressLine1: String? = "",
  @JsonProperty("primary_address_line2")
  val primaryAddressLine2: String? = "",
  @JsonProperty("primary_address_line3")
  val primaryAddressLine3: String? = "",
  @JsonProperty("primary_address_postcode")
  val primaryAddressPostCode: String? = "",
  @JsonProperty("phone_number1")
  val phoneOrMobileNumber: String? = "",

  // Many fields not present in AM order, only in normal orders

  val orderStartDate: String? = "",
  val orderEndDate: String? = "",
  val orderType: String? = "",
  val orderTypeDescription: String? = "",

  val enforceableCondition: String? = "",
  val orderEndOutcome: String? = "",
  val responsibleOrgDetailsPhoneNumber: String? = "",
  val responsibleOrgDetailsEmail: String? = "",
  val tagAtSource: String? = "",
  val specialInstructions: String? = "",
)
