SELECT
    legacy_subject_id
    , legacy_order_id
    , outcome
    , contact_type
    , reason
    , channel
    , user_id
    , user_name
    , contact_date
    , contact_time
    , modified_date
    , modified_time
FROM
    historic_api_mart_integration.contact_history
WHERE
    legacy_subject_id = 1401253