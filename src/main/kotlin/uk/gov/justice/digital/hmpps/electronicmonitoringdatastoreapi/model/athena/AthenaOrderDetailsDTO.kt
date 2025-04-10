package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

import com.fasterxml.jackson.annotation.JsonProperty

// TODO: Check which fields are nullable
data class AthenaOrderDetailsDTO(
  val legacySubjectId: String,
  val legacyOrderId: String?,
  val firstName: String? = "",
  val lastName: String? = "",
  val alias: String? = "",
  val dateOfBirth: String? = "",
  val adultOrChild: String? = "",
  val sex: String? = "",
  val contact: String? = "",
  @JsonProperty("primary_address_line_1")
  val primaryAddressLine1: String? = "",
  @JsonProperty("primary_address_line_2")
  val primaryAddressLine2: String? = "",
  @JsonProperty("primary_address_line_3")
  val primaryAddressLine3: String? = "",
  val primaryAddressPostCode: String? = "",
  val phoneOrMobileNumber: String? = "",
  val ppo: String? = "",
  val mappa: String? = "",
  val technicalBail: String? = "",
  val manualRisk: String? = "",
  val offenceRisk: Boolean,
  val postCodeRisk: String? = "",
  val falseLimbRisk: String? = "",
  val migratedRisk: String? = "",
  val rangeRisk: String? = "",
  val reportRisk: String? = "",
  val orderStartDate: String? = "",
  val orderEndDate: String? = "",
  val orderType: String? = "",
  val orderTypeDescription: String? = "",
  val orderTypeDetail: String? = "",
  val wearingWristPid: String? = "",
  val notifyingOrganisationDetailsName: String? = "",
  val responsibleOrganisation: String? = "",
  val responsibleOrganisationDetailsRegion: String? = "",
)
