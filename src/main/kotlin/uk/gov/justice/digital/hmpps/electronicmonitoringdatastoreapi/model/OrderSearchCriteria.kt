package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Pattern.Flag
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.validation.ValidOrderSearchCriteria
import java.time.LocalDate

@ValidOrderSearchCriteria
data class OrderSearchCriteria(

  @field:Pattern(regexp = "^[a-zA-Z0-9]+$", flags = [Flag.UNICODE_CASE])
  @field:Schema(description = "The legacy subject id of the order", required = false)
  val legacySubjectId: String? = null,

  @field:Pattern(regexp = "^[a-zA-Z]+$", flags = [Flag.UNICODE_CASE])
  @field:Schema(description = "The first name of the subject", required = false)
  val firstName: String? = null,

  @field:Pattern(regexp = "^[a-zA-Z]+$", flags = [Flag.UNICODE_CASE])
  @field:Schema(description = "The last name of the subject", required = false)
  val lastName: String? = null,

  @field:Pattern(regexp = "^[a-zA-Z\\s]+$", flags = [Flag.UNICODE_CASE])
  @field:Schema(description = "The alias of the subject", required = false)
  val alias: String? = null,

  @field:Schema(description = "The date of birth of the subject", required = false)
  val dateOfBirth: LocalDate? = null,
)
