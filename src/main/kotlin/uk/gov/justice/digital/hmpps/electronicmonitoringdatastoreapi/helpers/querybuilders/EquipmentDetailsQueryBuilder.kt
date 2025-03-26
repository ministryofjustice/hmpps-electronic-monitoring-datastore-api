package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaEquipmentDetailsListQuery

class EquipmentDetailsQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "equipment_details",
  arrayOf(
    "legacy_subject_id",
    "legacy_order_id",
    "hmu_id",
    "hmu_equipment_category_description",
    "hmu_install_date",
    "hmu_install_time",
    "hmu_removed_date",
    "hmu_removed_time",
    "pid_id",
    "pid_equipment_category_description",
    "date_device_installed",
    "time_device_installed",
    "date_device_removed",
    "time_device_removed",
  ),
) {
  fun withLegacySubjectId(legacySubjectId: String): EquipmentDetailsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isBlank()) {
      return this
    }

    values.add(legacySubjectId)
    whereClauses.put("legacy_subject_id", "legacy_subject_id" eq legacySubjectId)
    return this
  }

  fun build(): AthenaEquipmentDetailsListQuery = AthenaEquipmentDetailsListQuery(getSQL(), values.toTypedArray())
}
