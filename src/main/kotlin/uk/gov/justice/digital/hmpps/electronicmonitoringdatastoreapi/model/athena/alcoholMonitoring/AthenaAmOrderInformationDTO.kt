package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring

import com.fasterxml.jackson.annotation.JsonProperty

data class AthenaAmOrderInformationDTO(
  val legacySubjectId: String,
  val legacyOrderId: String,
  val firstName: String?,
  val lastName: String?,
  val alias: String?,
  val dateOfBirth: String?,
  @JsonProperty("primary_address_line_1")
  val address1: String?,
  @JsonProperty("primary_address_line_2")
  val address2: String?,
  @JsonProperty("primary_address_line_3")
  val address3: String?,
  @JsonProperty("primary_address_post_code")
  val postcode: String?,
  val orderStartDate: String?,
  val orderEndDate: String?,
)
