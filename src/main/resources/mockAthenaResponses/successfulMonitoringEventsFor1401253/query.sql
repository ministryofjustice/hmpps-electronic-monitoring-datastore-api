SELECT
    legacy_subject_id
    , legacy_order_id
    , event_type
    , event_date
    , event_time
    , event_second
    , process_date
    , process_time
    , process_second
FROM
    historic_api_mart_integration.event_history
WHERE
    legacy_subject_id = 1401253