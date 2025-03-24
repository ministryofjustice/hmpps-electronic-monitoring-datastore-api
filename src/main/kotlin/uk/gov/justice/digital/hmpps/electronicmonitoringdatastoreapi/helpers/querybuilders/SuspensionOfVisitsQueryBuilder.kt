package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSuspensionOfVisitsListQuery

class SuspensionOfVisitsQueryBuilder(
  override var databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "suspension_of_visits",
  arrayOf(
    "legacy_subject_id",
    "legacy_order_id",
    "suspension_of_visits",
    "suspension_of_visits_requested_date",
    "suspension_of_visits_start_date",
    "suspension_of_visits_start_time",
    "suspension_of_visits_end_date",
  ),
) {
  fun withLegacySubjectId(subjectId: String): SuspensionOfVisitsQueryBuilder {
    validateAlphanumeric(subjectId, "legacy_subject_id")

    if (subjectId.isBlank()) {
      return this
    }

    values.add(subjectId)
    whereClauses.put("legacy_subject_id", "legacy_subject_id" eq subjectId)
    return this
  }

  fun build(): AthenaSuspensionOfVisitsListQuery = AthenaSuspensionOfVisitsListQuery(getSQL(), values.toTypedArray())
}
