package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.annotation.Nullable
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.validation.ConditionalPattern
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.validation.ValidOrderSearchCriteria
import java.time.LocalDate

@ValidOrderSearchCriteria
data class OrderSearchCriteria(

  @field:Nullable
  @field:ConditionalPattern(regexp = "^[a-zA-Z0-9]+$")
  @field:Schema(description = "The legacy subject id of the order", required = false)
  val legacySubjectId: String? = null,

  @field:Nullable
  @field:ConditionalPattern(regexp = "^[a-zA-Z]+$")
  @field:Schema(description = "The first name of the subject", required = false)
  val firstName: String? = null,

  @field:Nullable
  @field:ConditionalPattern(regexp = "^[a-zA-Z]+$")
  @field:Schema(description = "The last name of the subject", required = false)
  val lastName: String? = null,

  @field:Nullable
  @field:ConditionalPattern(regexp = "^[a-zA-Z\\s]+$")
  @field:Schema(description = "The alias of the subject", required = false)
  val alias: String? = null,

  @field:Nullable
  @field:Schema(description = "The date of birth of the subject", required = false)
  val dateOfBirth: LocalDate? = null,
)
