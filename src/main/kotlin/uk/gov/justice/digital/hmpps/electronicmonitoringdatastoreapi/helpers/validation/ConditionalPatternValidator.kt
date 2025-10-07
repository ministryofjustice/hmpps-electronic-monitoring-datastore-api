package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.regex.Pattern

class ConditionalPatternValidator : ConstraintValidator<ConditionalPattern, String?> {
  private var pattern: Pattern? = null

  override fun initialize(constraintAnnotation: ConditionalPattern) {
    this.pattern = Pattern.compile(constraintAnnotation.regexp, Pattern.CASE_INSENSITIVE)
  }

  override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
    // If value is null or empty, skip pattern validation (let @NotEmpty handle it)
    if (value == null || value.trim { it <= ' ' }.isEmpty()) {
      return true
    }

    // Only validate pattern if value is not empty
    return pattern!!.matcher(value).matches()
  }
}
