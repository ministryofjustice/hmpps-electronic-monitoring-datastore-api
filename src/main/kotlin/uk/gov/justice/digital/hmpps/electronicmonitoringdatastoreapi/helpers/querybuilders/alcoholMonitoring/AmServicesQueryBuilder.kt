package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaAmServicesListQuery
import kotlin.collections.toTypedArray

class AmServicesQueryBuilder :
  SqlQueryBuilder(
    "am_services",
    arrayOf(
      "legacy_subject_id",
      "service_start_date",
      "service_end_date",
      "service_address",
      "equipment_start_date",
      "equipment_end_date",
      "hmu_serial_number",
      "device_serial_number",
    ),
  ) {
  fun withLegacySubjectId(legacySubjectId: String): AmServicesQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isBlank()) {
      return this
    }

    values.add(legacySubjectId)
    whereClauses.put("legacy_subject_id", "legacy_subject_id" eq legacySubjectId)
    return this
  }

  override fun build(databaseName: String): AthenaAmServicesListQuery = AthenaAmServicesListQuery(getSQL(databaseName), values.toTypedArray())
}
