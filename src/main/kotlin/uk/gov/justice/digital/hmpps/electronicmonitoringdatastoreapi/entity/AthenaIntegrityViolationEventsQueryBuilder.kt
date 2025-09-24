package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.SqlQueryBuilderBase

class AthenaIntegrityViolationEventsQueryBuilder :
  SqlQueryBuilderBase<AthenaIntegrityViolationEventDTO>(
    "violations",
    AthenaIntegrityViolationEventDTO::class,
  ) {
  fun withLegacySubjectId(legacySubjectId: String?): AthenaIntegrityViolationEventsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses["legacy_subject_id"] = "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')"
    return this
  }
}
