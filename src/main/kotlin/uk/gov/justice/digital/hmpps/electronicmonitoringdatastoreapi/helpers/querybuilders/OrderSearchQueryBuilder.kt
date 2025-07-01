package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import io.zeko.db.sql.dsl.eq
import io.zeko.db.sql.dsl.like
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchQuery
import java.time.LocalDate

class OrderSearchQueryBuilder(
  tableName: String,
) : SqlQueryBuilder(
  tableName,
  arrayOf(
    "legacy_subject_id",
    "first_name",
    "last_name",
    "alias",
    "date_of_birth",
    "primary_address_line_1",
    "primary_address_line_2",
    "primary_address_line_3",
    "primary_address_post_code",
    "order_start_date",
    "order_end_date",
  ),
) {
  fun withLegacySubjectId(value: String?): OrderSearchQueryBuilder {
    validateAlphanumeric(value, "legacy_subject_id")

    if (value.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$value')")
    whereClauses.put("legacy_subject_id", "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$value')")
    return this
  }

  fun withFirstName(value: String?): OrderSearchQueryBuilder {
    validateAlphanumericSpace(value, "first_name")

    if (value.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('%$value%')")
    whereClauses.put("first_name", "UPPER(first_name)" like "UPPER('%$value%')")
    return this
  }

  fun withLastName(value: String?): OrderSearchQueryBuilder {
    validateAlphanumericSpace(value, "last_name")

    if (value.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('%$value%')")
    whereClauses.put("last_name", "UPPER(last_name)" like "UPPER('%$value%')")
    return this
  }

  fun withAlias(value: String?): OrderSearchQueryBuilder {
    validateAlphanumericSpace(value, "alias")

    if (value.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('%$value%')")
    whereClauses.put("alias", "UPPER(alias)" like "UPPER('%$value%')")
    return this
  }

  fun withDob(dateOfBirth: LocalDate?): OrderSearchQueryBuilder {
    if (dateOfBirth == null) {
      return this
    }

    values.add("DATE '$dateOfBirth'")
    whereClauses.put("date_of_birth", "date_of_birth" eq "DATE '$dateOfBirth'")

    return this
  }

  override fun build(databaseName: String): AthenaOrderSearchQuery {
    if (whereClauses.isEmpty()) {
      throw IllegalArgumentException("At least one search criteria must be populated")
    }

    return AthenaOrderSearchQuery(getSQL(databaseName), values.toTypedArray())
  }
}
