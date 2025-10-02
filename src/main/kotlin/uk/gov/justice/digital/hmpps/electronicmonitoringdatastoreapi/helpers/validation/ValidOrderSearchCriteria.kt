package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [OrderSearchCriteriaValidator::class])
annotation class ValidOrderSearchCriteria(
  val message: String = "This request is malformed, there must be at least one search criteria present",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
)
