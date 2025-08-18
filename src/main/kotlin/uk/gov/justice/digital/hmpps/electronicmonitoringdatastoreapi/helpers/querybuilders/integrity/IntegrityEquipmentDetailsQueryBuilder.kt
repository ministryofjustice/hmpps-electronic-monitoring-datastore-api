package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.AthenaEquipmentDetailsListQuery

class IntegrityEquipmentDetailsQueryBuilder :
  SqlQueryBuilder(
    "equipment_details",
    arrayOf(
      "legacy_subject_id",
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
  fun withLegacySubjectId(legacySubjectId: String?): IntegrityEquipmentDetailsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isNullOrBlank()) {
      return this
    }

    values.add("UPPER('$legacySubjectId')")
    whereClauses.put("legacy_subject_id", "UPPER(CAST(legacy_subject_id as varchar))" eq "UPPER('$legacySubjectId')")
    return this
  }

  override fun build(databaseName: String): AthenaEquipmentDetailsListQuery = AthenaEquipmentDetailsListQuery(getSQL(databaseName), values.toTypedArray())
}
