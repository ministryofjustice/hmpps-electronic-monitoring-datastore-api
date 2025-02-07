SELECT
    legacy_subject_id
     , legacy_order_id
     , hmu_id
     , hmu_equipment_category_description
     , hmu_install_date
     , hmu_install_time
     , hmu_removed_date
     , hmu_removed_time
     , pid_id
     , pid_equipment_category_description
     , date_device_installed
     , time_device_installed
     , date_device_removed
     , time_device_removed
FROM
    historic_api_mart_integration.equipment_details
WHERE
    legacy_subject_id = 1401253