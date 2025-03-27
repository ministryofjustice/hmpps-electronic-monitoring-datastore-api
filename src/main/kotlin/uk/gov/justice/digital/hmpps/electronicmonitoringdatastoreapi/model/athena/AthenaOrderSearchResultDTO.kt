package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

import com.fasterxml.jackson.annotation.JsonProperty

data class AthenaOrderSearchResultDTO(
  val legacySubjectId: Long,
  val legacyOrderId: String? = "",
  val fullName: String? = "",
  val alias: String? = "",
  @JsonProperty("primary_address_line_1")
  val primaryAddressLine1: String? = "",
  @JsonProperty("primary_address_line_2")
  val primaryAddressLine2: String? = "",
  @JsonProperty("primary_address_line_3")
  val primaryAddressLine3: String? = "",
  val primaryAddressPostCode: String? = "",
  val dateOfBirth: String? = "",
  val orderStartDate: String? = "",
  val orderEndDate: String? = "",
)

data class AthenaOrderSearchResults(
  val results: List<AthenaOrderSearchResultDTO>,
  val queryExecutionId: String,
)
