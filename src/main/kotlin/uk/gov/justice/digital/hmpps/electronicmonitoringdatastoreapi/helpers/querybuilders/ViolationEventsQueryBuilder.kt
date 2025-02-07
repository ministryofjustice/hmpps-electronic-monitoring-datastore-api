package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaViolationEventsListQuery

class ViolationEventsQueryBuilder(
  var databaseName: String? = null,
) {
  var legacySubjectId: String? = null

  fun withLegacySubjectId(subjectId: String): ViolationEventsQueryBuilder {
    legacySubjectId = subjectId
    return this
  }

  fun build(): AthenaViolationEventsListQuery = AthenaViolationEventsListQuery(
    """
      SELECT
          legacy_subject_id
        , legacy_order_id
        , enforcement_reason
        , investigation_outcome_reason
        , breach_details
        , breach_enforcement_outcome
        , agency_action
        , breach_date
        , breach_time
        , breach_identified_date
        , breach_identified_time
        , authority_first_notified_date
        , authority_first_notified_time
        , agency_response_date
        , breach_pack_requested_date
        , breach_pack_sent_date
        , section_9_date
        , hearing_date
        , summons_served_date
        , subject_letter_sent_date
        , warning_letter_sent_date
        , warning_letter_sent_time
      FROM
        $databaseName.violations
      WHERE
        legacy_subject_id = $legacySubjectId
    """.trimIndent(),
  )
}
