SELECT
    legacy_order_id
     , legacy_subject_id
     , first_name
     , last_name
     , alias
     , date_of_birth
     , legacy_gender
     , primary_address_line1
     , primary_address_line2
     , primary_address_line3
     , primary_address_postcode
     , phone_number1
     , order_start_date
     , order_end_date
     , order_type
     , order_type_description
     , enforceable_condition
     , order_end_outcome
     , responsible_org_details_phone_number
     , responsible_org_details_email
     , tag_at_source
     , special_instructions
FROM
    historic_api_mart_integration.am_order_details
WHERE
    legacy_order_id = '1401254'