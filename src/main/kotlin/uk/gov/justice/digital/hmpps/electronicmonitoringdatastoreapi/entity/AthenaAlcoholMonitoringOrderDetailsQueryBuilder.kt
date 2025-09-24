package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity

import io.zeko.db.sql.dsl.eq
import io.zeko.db.sql.dsl.like
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.SqlQueryBuilderBase
import java.time.LocalDate

class AthenaAlcoholMonitoringOrderDetailsQueryBuilder :
  SqlQueryBuilderBase<AthenaAlcoholMonitoringOrderDetailsDTO>(
    "am_order_details",
    AthenaAlcoholMonitoringOrderDetailsDTO::class,
  ) {
  fun withLegacySubjectId(legacySubjectId: String?): AthenaAlcoholMonitoringOrderDetailsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses["legacy_subject_id"] = "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')"
    return this
  }

  fun withFirstName(value: String?): AthenaAlcoholMonitoringOrderDetailsQueryBuilder {
    validateAlphanumericSpace(value, "first_name")

    if (value.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('%$value%')")
    whereClauses["first_name"] = "UPPER(first_name)" like "UPPER('%$value%')"
    return this
  }

  fun withLastName(value: String?): AthenaAlcoholMonitoringOrderDetailsQueryBuilder {
    validateAlphanumericSpace(value, "last_name")

    if (value.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('%$value%')")
    whereClauses["last_name"] = "UPPER(last_name)" like "UPPER('%$value%')"
    return this
  }

  fun withAlias(value: String?): AthenaAlcoholMonitoringOrderDetailsQueryBuilder {
    validateAlphanumericSpace(value, "alias")

    if (value.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('%$value%')")
    whereClauses["alias"] = "UPPER(alias)" like "UPPER('%$value%')"
    return this
  }

  fun withDob(dateOfBirth: LocalDate?): AthenaAlcoholMonitoringOrderDetailsQueryBuilder {
    if (dateOfBirth == null) {
      return this
    }

    values.add("DATE '$dateOfBirth'")
    whereClauses["date_of_birth"] = "date_of_birth" eq "DATE '$dateOfBirth'"

    return this
  }
}
