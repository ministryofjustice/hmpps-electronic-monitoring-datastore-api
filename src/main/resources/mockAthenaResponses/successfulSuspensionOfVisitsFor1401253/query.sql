SELECT
    legacy_subject_id
     , suspension_of_visits
     , suspension_of_visits_requested_date
     , suspension_of_visits_start_date
     , suspension_of_visits_start_time
     , suspension_of_visits_end_date
FROM
    historic_api_mart.suspension_of_visits
WHERE
    legacy_subject_id = 1401253;