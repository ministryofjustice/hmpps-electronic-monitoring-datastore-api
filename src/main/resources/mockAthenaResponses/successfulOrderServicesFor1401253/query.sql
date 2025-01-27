SELECT
    legacy_subject_id
    , service_id
    , service_address_1
    , service_address_2
    , service_address_3
    , service_address_postcode
    , service_start_date
    , service_end_date
    , curfew_start_date
    , curfew_end_date
    , curfew_start_time
    , curfew_end_time
    , monday
    , tuesday
    , wednesday
    , thursday
    , friday
    , saturday
    , sunday
FROM
    historic_api_mart_integration.services
WHERE
    legacy_subject_id = 1401253