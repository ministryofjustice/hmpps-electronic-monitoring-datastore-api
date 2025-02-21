package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.apache.commons.lang3.StringUtils.isAlphanumericSpace
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchQuery

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

  var dobDay: String? = null
    private set(value) {
      if (value == null) {
        return
      }

      try {
        value.toLong()
      } catch (_: Exception) {
        throw IllegalArgumentException("date_of_birth_day must be convertable to type Long")
      }

      field = value
    }

  var dobMonth: String? = null
    private set(value) {
      if (value == null) {
        return
      }

      try {
        value.toLong()
      } catch (_: Exception) {
        throw IllegalArgumentException("date_of_birth_month must be convertable to type Long")
      }

      field = value
    }

  var dobYear: String? = null
    private set(value) {
      if (value == null) {
        return
      }

      try {
        value.toLong()
      } catch (_: Exception) {
        throw IllegalArgumentException("date_of_birth_year must be convertable to type Long")
      }

      field = value
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

    // builder.append(whereClause.map{ "${it.key} LIKE '%${it.value}%'" }.joinToString(separator = " OR "))
//    builder.append(whereClause.map { "${it.key}=${it.value}" }.joinToString(separator = " OR "))
    builder.append(whereClause.values.joinToString(separator = " OR "))

    return AthenaOrderSearchQuery(builder.toString())
  }
}
