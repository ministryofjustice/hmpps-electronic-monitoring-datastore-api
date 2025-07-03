package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.models.AthenaContactEventsListQuery

class IntegrityContactEventsQueryBuilder :
  SqlQueryBuilder(
    "contact_history",
    arrayOf(
      "legacy_subject_id",
      "outcome",
      "contact_type",
      "reason",
      "channel",
      "user_id",
      "user_name",
      "contact_date",
      "contact_time",
      "modified_date",
      "modified_time",
    ),
  ) {
  fun withLegacySubjectId(legacySubjectId: String?): IntegrityContactEventsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses.put("legacy_subject_id", "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')")
    return this
  }

  override fun build(databaseName: String): AthenaContactEventsListQuery = AthenaContactEventsListQuery(getSQL(databaseName), values.toTypedArray())
}
