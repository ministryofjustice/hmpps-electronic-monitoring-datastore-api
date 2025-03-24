package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.apache.commons.lang3.StringUtils.isAlphanumeric
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaViolationEventsListQuery

class ViolationEventsQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "violations",
  arrayOf(
    "legacy_subject_id",
    "legacy_order_id",
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
  fun withLegacySubjectId(subjectId: String): ViolationEventsQueryBuilder {
    if (!isAlphanumeric(subjectId)) {
      throw IllegalArgumentException("Input contains illegal characters")
    }

    parameters["legacy_subject_id"] = subjectId
    return this
  }

  fun build(): AthenaViolationEventsListQuery = AthenaViolationEventsListQuery(getSQL(), parameters.values.toTypedArray())
}
