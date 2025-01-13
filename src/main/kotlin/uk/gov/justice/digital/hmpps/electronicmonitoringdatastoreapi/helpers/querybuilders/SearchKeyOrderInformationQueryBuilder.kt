package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery

class SearchKeyOrderInformationQueryBuilder {
  private var whereClause = mutableMapOf<String, String>()

  var legacySubjectId: String? = null
    private set(value) {
      try {
        value!!.toLong()
      } catch (_: Exception) {
        throw IllegalArgumentException("Legacy_subject_id must be convertable to type Long")
      }

      whereClause.put("legacy_subject_id", value)
      field = value
    }

  fun withLegacySubjectId(value: String?): SearchKeyOrderInformationQueryBuilder {
    legacySubjectId = value
    return this
  }

  var firstName: String? = null
    private set(value) {
      whereClause.put("first_name", "upper('$value')")
      field = value
    }

  fun withFirstName(value: String?): SearchKeyOrderInformationQueryBuilder {
    firstName = value
    return this
  }

  var lastName: String? = null
    private set(value) {
      whereClause.put("last_name", "upper('$value')")
      field = value
    }

  fun withLastName(value: String?): SearchKeyOrderInformationQueryBuilder {
    lastName = value
    return this
  }

  var alias: String? = null
    private set(value) {
      whereClause.put("alias", "upper('$value')")
      field = value
    }

  fun withAlias(value: String?): SearchKeyOrderInformationQueryBuilder {
    alias = value
    return this
  }

  var dobDay: String? = null
    private set(value) {
      try {
        value!!.toLong()
      } catch (_: Exception) {
        throw IllegalArgumentException("date_of_birth_day must be convertable to type Long")
      }

      field = value
    }

  var dobMonth: String? = null
    private set(value) {
      try {
        value!!.toLong()
      } catch (_: Exception) {
        throw IllegalArgumentException("date_of_birth_month must be convertable to type Long")
      }

      field = value
    }

  var dobYear: String? = null
    private set(value) {
      try {
        value!!.toLong()
      } catch (_: Exception) {
        throw IllegalArgumentException("date_of_birth_year must be convertable to type Long")
      }

      field = value
    }

  fun build(): AthenaQuery<AthenaKeyOrderInformationDTO> {
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
            test_database.order_details
          WHERE 
      """.trimIndent(),
    )

    builder.append(whereClause.entries.joinToString(separator = " OR "))

    return AthenaQuery(builder.toString())
  }
}
