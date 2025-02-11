SELECT
    legacy_subject_id
    , legacy_order_id
    , address_1
    , address_2
    , address_3
    , postcode
    , actual_work_start_date
    , actual_work_start_time
    , actual_work_end_date
    , actual_work_end_time
    , visit_notes
    , visit_type
    , visit_outcome
FROM
    historic_api_mart_integration.visit_details
WHERE
    legacy_subject_id = 1401253