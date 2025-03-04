package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.apache.commons.lang3.StringUtils.isAlphanumericSpace
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchQuery
import java.time.LocalDate

// TODO: update to use PREPARED STATEMENTS instead of a plain SELECT: better injection protection.
class OrderSearchQueryBuilder(
  var databaseName: String? = null,
) {
  private var whereClause = mutableMapOf<String, String>()

  var legacySubjectId: String? = null
    private set(value) {
      if (value == null) {
        return
      }

      try {
        value.toLong()
      } catch (_: Exception) {
        throw IllegalArgumentException("Legacy_subject_id must be convertable to type Long")
      }

      whereClause.put("legacy_subject_id", "legacy_subject_id=$value")
      field = value
    }

  fun withLegacySubjectId(value: String?): OrderSearchQueryBuilder {
    legacySubjectId = value
    return this
  }

  var firstName: String? = null
    private set(value) {
      if (value == null || value.trim().isEmpty()) {
        return
      }

      whereClause.put("first_name", "first_name LIKE UPPER('%$value%')")
      field = value
    }

  fun withFirstName(value: String?): OrderSearchQueryBuilder {
    if (!isAlphanumericSpace(value ?: "")) {
      throw IllegalArgumentException("Input contains illegal characters")
    }
    firstName = value
    return this
  }

  var lastName: String? = null
    private set(value) {
      if (value == null || value.trim().isEmpty()) {
        return
      }

      whereClause.put("last_name", "last_name LIKE UPPER('%$value%')")
      field = value
    }

  fun withLastName(value: String?): OrderSearchQueryBuilder {
    if (!isAlphanumericSpace(value ?: "")) {
      throw IllegalArgumentException("Input contains illegal characters")
    }
    lastName = value
    return this
  }

  var alias: String? = null
    private set(value) {
      if (value == null || value.trim().isEmpty()) {
        return
      }

      whereClause.put("alias", "alias LIKE UPPER('%$value%')")
      field = value
    }

  fun withAlias(value: String?): OrderSearchQueryBuilder {
    if (!isAlphanumericSpace(value ?: "")) {
      throw IllegalArgumentException("Input contains illegal characters")
    }

    alias = value
    return this
  }

  fun withDob(day: String?, month: String?, year: String?): OrderSearchQueryBuilder {
    val dobDay: String? = validateNumber(day, "date_of_birth_day")
    val dobMonth: String? = validateNumber(month, "date_of_birth_month")
    val dobYear: String? = validateNumber(year, "date_of_birth_year")

    if (!dobDay.isNullOrEmpty() && !dobMonth.isNullOrEmpty() && !dobYear.isNullOrEmpty()) {
      val dateOfBirth = LocalDate.of(dobYear.toInt(), dobMonth.toInt(), dobDay.toInt())
      whereClause.put("date_of_birth", "date_of_birth = DATE '$dateOfBirth'")
    }
    return this
  }

  private fun validateNumber(value: String?, field: String): String? {
    if (!value.isNullOrEmpty()) {
      try {
        value.toInt()
      } catch (_: Exception) {
        throw IllegalArgumentException("$field must be convertable to type Int")
      }
    }
    return value
  }

  fun build(): AthenaOrderSearchQuery {
    if (whereClause.isEmpty()) {
      throw IllegalArgumentException("At least one search criteria must be populated")
    }

    val builder: StringBuilder = StringBuilder()
    builder.append(
      """
        SELECT
          legacy_subject_id
          , full_name
          , primary_address_line_1
          , primary_address_line_2
          , primary_address_line_3
          , primary_address_post_code
          , order_start_date
          , order_end_date
        FROM
          $databaseName.order_details
        WHERE 
      """.trimIndent(),
    )

    builder.append(whereClause.values.joinToString(separator = " OR "))

    return AthenaOrderSearchQuery(builder.toString())
  }
}
