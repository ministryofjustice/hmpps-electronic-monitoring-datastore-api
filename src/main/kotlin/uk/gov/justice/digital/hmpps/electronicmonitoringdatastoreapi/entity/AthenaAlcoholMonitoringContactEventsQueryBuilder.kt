package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.SqlQueryBuilderBase

class AthenaAlcoholMonitoringContactEventsQueryBuilder :
  SqlQueryBuilderBase<AthenaAlcoholMonitoringContactEventDTO>(
    "am_contact_history",
    AthenaAlcoholMonitoringContactEventDTO::class,
  ) {
  fun withLegacySubjectId(legacySubjectId: String?): AthenaAlcoholMonitoringContactEventsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses["legacy_subject_id"] = "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')"
    return this
  }
}
