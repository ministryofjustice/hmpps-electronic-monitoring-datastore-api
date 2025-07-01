package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaAmEquipmentDetailsListQuery
import kotlin.collections.toTypedArray

class AmEquipmentDetailsQueryBuilder :
  SqlQueryBuilder(
    "am_equipment_details",
    arrayOf(
      "legacy_subject_id",
      "device_type",
      "device_serial_number",
      "device_address_type",
      "leg_fitting",
      "date_device_installed",
      "time_device_installed",
      "date_device_removed",
      "time_device_removed",
      "hmu_install_date",
      "hmu_install_time",
      "hmu_removed_date",
      "hmu_removed_time",
    ),
  ) {
  fun withLegacySubjectId(legacySubjectId: String?): AmEquipmentDetailsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses.put("legacy_subject_id", "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')")
    return this
  }

  override fun build(databaseName: String): AthenaAmEquipmentDetailsListQuery = AthenaAmEquipmentDetailsListQuery(getSQL(databaseName), values.toTypedArray())
}
