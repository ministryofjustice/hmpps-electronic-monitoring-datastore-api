SELECT
    legacy_subject_id
    , legacy_order_id
    , violation_alert_type
    , violation_alert_date
    , violation_alert_time
FROM
    historic_api_mart_integration.incident
WHERE
    legacy_subject_id = 1401253