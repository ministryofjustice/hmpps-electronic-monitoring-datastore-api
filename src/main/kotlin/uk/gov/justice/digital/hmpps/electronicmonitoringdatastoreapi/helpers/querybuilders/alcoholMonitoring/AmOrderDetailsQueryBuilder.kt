package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring

import io.zeko.db.sql.dsl.eq
import io.zeko.db.sql.dsl.like
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.AthenaAmOrderDetailsQuery
import java.time.LocalDate

class AmOrderDetailsQueryBuilder :
  SqlQueryBuilder(
    "am_order_details",
    arrayOf(
      "legacy_subject_id",
      "legacy_order_id",
      "first_name",
      "last_name",
      "alias",
      "date_of_birth",
      "legacy_gender",
      "special_instructions",
      "phone_or_mobile_number",
      "primary_address_line_1",
      "primary_address_line_2",
      "primary_address_line_3",
      "primary_address_post_code",
      "order_start_date",
      "order_end_date",
      "enforceable_condition",
      "order_type",
      "order_type_description",
      "order_end_outcome",
      "responsible_org_details_phone_number",
      "responsible_org_details_email",
      "tag_at_source",
    ),
  ) {
  fun withLegacySubjectId(legacySubjectId: String?): AmOrderDetailsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses.put("legacy_subject_id", "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')")
    return this
  }

  fun withFirstName(value: String?): AmOrderDetailsQueryBuilder {
    validateAlphanumericSpace(value, "first_name")

    if (value.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('%$value%')")
    whereClauses.put("first_name", "UPPER(first_name)" like "UPPER('%$value%')")
    return this
  }

  fun withLastName(value: String?): AmOrderDetailsQueryBuilder {
    validateAlphanumericSpace(value, "last_name")

    if (value.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('%$value%')")
    whereClauses.put("last_name", "UPPER(last_name)" like "UPPER('%$value%')")
    return this
  }

  fun withAlias(value: String?): AmOrderDetailsQueryBuilder {
    validateAlphanumericSpace(value, "alias")

    if (value.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('%$value%')")
    whereClauses.put("alias", "UPPER(alias)" like "UPPER('%$value%')")
    return this
  }

  fun withDob(dateOfBirth: LocalDate?): AmOrderDetailsQueryBuilder {
    if (dateOfBirth == null) {
      return this
    }

    values.add("DATE '$dateOfBirth'")
    whereClauses.put("date_of_birth", "date_of_birth" eq "DATE '$dateOfBirth'")

    return this
  }

  override fun build(databaseName: String): AthenaAmOrderDetailsQuery = AthenaAmOrderDetailsQuery(getSQL(databaseName), values.toTypedArray())
}
