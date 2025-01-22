SELECT
  legacy_subject_id
  , full_name
  , primary_address_line_1
  , primary_address_line_2
  , primary_address_line_3
  , primary_address_post_code
  , order_start_date
  , order_end_date
FROM
    historic_api_mart_integration.order_details
WHERE
    first_name=upper('any')