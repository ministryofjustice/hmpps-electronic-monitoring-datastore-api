package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaAmServicesListQuery
import kotlin.collections.toTypedArray

class AmContactEventsQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "am_contact_history",
  arrayOf(
    "legacy_subject_id",
    "legacy_order_id",
    "contact_date",
    "contact_time",
    "inbound_or_outbound",
    "from_to",
    "channel",
    "subject_consent_withdrawn",
    "call_outcome",
    "statement",
    "reason_for_contact",
    "outcome_of_contact",
    "visit_required",
    "visit_id",
  ),
) {
  fun withLegacySubjectId(legacySubjectId: String): AmContactEventsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isBlank()) {
      return this
    }

    values.add(legacySubjectId)
    whereClauses.put("legacy_subject_id", "legacy_subject_id" eq legacySubjectId)
    return this
  }

  fun build(): AthenaAmServicesListQuery = AthenaAmServicesListQuery(getSQL(), values.toTypedArray())
}
