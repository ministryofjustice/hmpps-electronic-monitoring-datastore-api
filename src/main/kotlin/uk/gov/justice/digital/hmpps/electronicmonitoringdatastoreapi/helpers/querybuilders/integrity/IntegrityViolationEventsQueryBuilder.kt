package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.AthenaViolationEventsListQuery

class IntegrityViolationEventsQueryBuilder :
  SqlQueryBuilder(
    "violations",
    arrayOf(
      "legacy_subject_id",
      "enforcement_reason",
      "investigation_outcome_reason",
      "breach_details",
      "breach_enforcement_outcome",
      "agency_action",
      "breach_date",
      "breach_time",
      "breach_identified_date",
      "breach_identified_time",
      "authority_first_notified_date",
      "authority_first_notified_time",
      "agency_response_date",
      "breach_pack_requested_date",
      "breach_pack_sent_date",
      "section_9_date",
      "hearing_date",
      "summons_served_date",
      "subject_letter_sent_date",
      "warning_letter_sent_date",
      "warning_letter_sent_time",
    ),
  ) {
  fun withLegacySubjectId(legacySubjectId: String?): IntegrityViolationEventsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses.put("legacy_subject_id", "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')")
    return this
  }

  override fun build(databaseName: String): AthenaViolationEventsListQuery = AthenaViolationEventsListQuery(getSQL(databaseName), values.toTypedArray())
}
