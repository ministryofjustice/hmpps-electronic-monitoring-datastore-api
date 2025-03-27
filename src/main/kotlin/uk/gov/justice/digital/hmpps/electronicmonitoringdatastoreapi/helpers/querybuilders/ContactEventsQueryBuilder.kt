package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventsListQuery

class ContactEventsQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "contact_history",
  arrayOf(
    "legacy_subject_id",
    "legacy_order_id",
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
  fun withLegacySubjectId(legacySubjectId: String): ContactEventsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isBlank()) {
      return this
    }

    values.add(legacySubjectId)
    whereClauses.put("legacy_subject_id", "legacy_subject_id" eq legacySubjectId)
    return this
  }

  fun build(): AthenaContactEventsListQuery = AthenaContactEventsListQuery(getSQL(), values.toTypedArray())
}
