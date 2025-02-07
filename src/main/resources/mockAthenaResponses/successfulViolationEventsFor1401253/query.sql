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
    historic_api_mart_integration.violations
WHERE
    legacy_subject_id = 1401253