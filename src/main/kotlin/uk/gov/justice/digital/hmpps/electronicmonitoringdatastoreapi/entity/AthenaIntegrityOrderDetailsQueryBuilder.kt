package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity

import io.zeko.db.sql.dsl.eq
import io.zeko.db.sql.dsl.like
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.SqlQueryBuilderBase
import java.time.LocalDate

class AthenaIntegrityOrderDetailsQueryBuilder :
  SqlQueryBuilderBase<AthenaIntegrityOrderDetailsDTO>(
    "order_details",
    AthenaIntegrityOrderDetailsDTO::class,
  ) {
  fun withLegacySubjectId(legacySubjectId: String?): AthenaIntegrityOrderDetailsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses["legacy_subject_id"] = "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')"
    return this
  }

  fun withFirstName(value: String?): AthenaIntegrityOrderDetailsQueryBuilder {
    validateAlphanumericSpace(value, "first_name")

    if (value.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('%$value%')")
    whereClauses["first_name"] = "UPPER(first_name)" like "UPPER('%$value%')"
    return this
  }

  fun withLastName(value: String?): AthenaIntegrityOrderDetailsQueryBuilder {
    validateAlphanumericSpace(value, "last_name")

    if (value.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('%$value%')")
    whereClauses["last_name"] = "UPPER(last_name)" like "UPPER('%$value%')"
    return this
  }

  fun withAlias(value: String?): AthenaIntegrityOrderDetailsQueryBuilder {
    validateAlphanumericSpace(value, "alias")

    if (value.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('%$value%')")
    whereClauses["alias"] = "UPPER(alias)" like "UPPER('%$value%')"
    return this
  }

  fun withDob(dateOfBirth: LocalDate?): AthenaIntegrityOrderDetailsQueryBuilder {
    if (dateOfBirth == null) {
      return this
    }

    values.add("DATE '$dateOfBirth'")
    whereClauses["date_of_birth"] = "date_of_birth" eq "DATE '$dateOfBirth'"

    return this
  }
}
