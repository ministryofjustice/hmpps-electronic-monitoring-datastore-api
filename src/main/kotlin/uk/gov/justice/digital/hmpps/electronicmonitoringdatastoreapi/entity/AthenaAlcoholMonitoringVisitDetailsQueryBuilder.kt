package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.SqlQueryBuilderBase

class AthenaAlcoholMonitoringVisitDetailsQueryBuilder :
  SqlQueryBuilderBase<AthenaAlcoholMonitoringVisitDetailsDTO>(
    "am_visit_details",
    AthenaAlcoholMonitoringVisitDetailsDTO::class,
  ) {
  fun withLegacySubjectId(legacySubjectId: String?): AthenaAlcoholMonitoringVisitDetailsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses["legacy_subject_id"] = "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')"
    return this
  }
}
