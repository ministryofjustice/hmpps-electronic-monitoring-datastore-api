package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import java.time.LocalDate
import kotlin.reflect.KClass

@ValidOrderSearchCriteria
data class OrderSearchCriteria(

  @field:Schema(description = "The legacy subject id of the order", required = false)
  val legacySubjectId: String? = null,

  @field:Schema(description = "The first name of the subject", required = false)
  val firstName: String? = null,

  @field:Schema(description = "The last name of the subject", required = false)
  val lastName: String? = null,

  @field:Schema(description = "The alias of the subject", required = false)
  val alias: String? = null,

  @field:Schema(description = "The date of birth of the subject", required = false)
  val dateOfBirth: LocalDate? = null,
)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [OrderSearchCriteriaValidator::class])
annotation class ValidOrderSearchCriteria(
  val message: String = "This request is malformed, there must be at least one search criteria present",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
)

class OrderSearchCriteriaValidator : ConstraintValidator<ValidOrderSearchCriteria, OrderSearchCriteria> {
  override fun isValid(
    value: OrderSearchCriteria?,
    context: ConstraintValidatorContext?,
  ): Boolean {
    if (value == null) {
      return false
    }

    if (value.legacySubjectId == null && value.firstName == null && value.lastName == null && value.alias == null && value.dateOfBirth == null) {
      return false
    }

    return true
  }

  fun message(): String = "Sorry, passwords does not match"
}
