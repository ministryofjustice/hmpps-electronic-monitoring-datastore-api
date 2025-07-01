package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSuspensionOfVisitsListQuery

class IntegritySuspensionOfVisitsQueryBuilder :
  SqlQueryBuilder(
    "suspension_of_visits",
    arrayOf(
      "legacy_subject_id",
      "suspension_of_visits",
      "suspension_of_visits_requested_date",
      "suspension_of_visits_start_date",
      "suspension_of_visits_start_time",
      "suspension_of_visits_end_date",
    ),
  ) {
  fun withLegacySubjectId(legacySubjectId: String?): IntegritySuspensionOfVisitsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses.put("legacy_subject_id", "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')")
    return this
  }

  override fun build(databaseName: String): AthenaSuspensionOfVisitsListQuery = AthenaSuspensionOfVisitsListQuery(getSQL(databaseName), values.toTypedArray())
}
