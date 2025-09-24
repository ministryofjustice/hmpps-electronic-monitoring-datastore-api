package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.SqlQueryBuilderBase

class AthenaIntegritySuspensionOfVisitsQueryBuilder :
  SqlQueryBuilderBase<AthenaIntegritySuspensionOfVisitsDTO>(
    "suspension_of_visits",
    AthenaIntegritySuspensionOfVisitsDTO::class,
  ) {
  fun withLegacySubjectId(legacySubjectId: String?): AthenaIntegritySuspensionOfVisitsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses["legacy_subject_id"] = "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')"
    return this
  }
}
