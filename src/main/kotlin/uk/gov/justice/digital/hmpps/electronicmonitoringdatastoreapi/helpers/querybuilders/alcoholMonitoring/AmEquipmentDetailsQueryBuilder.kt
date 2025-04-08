package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaAmEquipmentDetailsListQuery
import kotlin.collections.toTypedArray

class AmEquipmentDetailsQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
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
  fun withLegacySubjectId(legacySubjectId: String): AmEquipmentDetailsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isBlank()) {
      return this
    }

    values.add(legacySubjectId)
    whereClauses.put("legacy_subject_id", "legacy_subject_id" eq legacySubjectId)
    return this
  }

  fun build(): AthenaAmEquipmentDetailsListQuery = AthenaAmEquipmentDetailsListQuery(getSQL(), values.toTypedArray())
}
