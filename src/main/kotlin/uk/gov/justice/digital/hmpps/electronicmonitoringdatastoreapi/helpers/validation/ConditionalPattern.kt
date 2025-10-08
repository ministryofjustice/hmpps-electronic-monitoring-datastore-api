package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.annotation.Retention
import kotlin.annotation.Target
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ConditionalPatternValidator::class])
annotation class ConditionalPattern(
  val message: String = "Pattern validation failed",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
  val regexp: String,
)
