package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.alcoholMonitoring

import com.fasterxml.jackson.annotation.JsonProperty

data class AthenaAmOrderDetailsDTO(
  val legacySubjectId: String,
  val legacyOrderId: String?,
  val firstName: String?,
  val lastName: String?,
  val alias: String?,
  val dateOfBirth: String?,
  val legacyGender: String?,
  val specialInstructions: String?,
  val phoneOrMobileNumber: String?,
  val primaryAddressLine1: String?,
  val primaryAddressLine2: String?,
  val primaryAddressLine3: String?,
  val primaryAddressPostCode: String?,
  val orderStartDate: String?,
  val orderEndDate: String?,
  val enforceableCondition: String?,
  val orderType: String?,
  val orderTypeDescription: String?,
  val orderEndOutcome: String?,
  @param:JsonProperty("responsible_org_details_phone_number")
  val responsibleOrganisationPhoneNumber: String?,
  @param:JsonProperty("responsible_org_details_email")
  val responsibleOrganisationEmail: String?,
  val tagAtSource: String?,
)
