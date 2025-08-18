package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.AthenaAmServicesListQuery
import kotlin.collections.toTypedArray

class AmContactEventsQueryBuilder :
  SqlQueryBuilder(
    "am_contact_history",
    arrayOf(
      "legacy_subject_id",
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
  fun withLegacySubjectId(legacySubjectId: String?): AmContactEventsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses.put("legacy_subject_id", "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')")
    return this
  }

  override fun build(databaseName: String): AthenaAmServicesListQuery = AthenaAmServicesListQuery(getSQL(databaseName), values.toTypedArray())
}
