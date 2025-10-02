package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers

import com.fasterxml.jackson.databind.PropertyNamingStrategies

// NOTE: This SNAKE_CASE strategy also adds underscores before numbers which we need
class AlphanumericSnakeCaseStrategy : PropertyNamingStrategies.NamingBase() {
  override fun translate(input: String?): String? = if (input == null) {
    null
  } else {
    "([A-Z]+|[0-9]+)".toRegex().replace(input) { "_${it.groupValues[1]}".lowercase() }
  }
}
