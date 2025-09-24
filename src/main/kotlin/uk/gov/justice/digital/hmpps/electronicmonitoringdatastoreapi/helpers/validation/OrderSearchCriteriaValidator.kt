package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria

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

  fun message(): String = "This request is malformed, there must be at least one search criteria present"
}
