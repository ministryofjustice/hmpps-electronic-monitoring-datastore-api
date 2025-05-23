package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity

import io.zeko.db.sql.dsl.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaServiceDetailsQuery

class IntegrityServiceDetailsQueryBuilder(
  override var databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "services",
  arrayOf(
    "legacy_subject_id",
    "service_id",
    "service_address_1",
    "service_address_2",
    "service_address_3",
    "service_address_postcode",
    "service_start_date",
    "service_end_date",
    "curfew_start_date",
    "curfew_end_date",
    "curfew_start_time",
    "curfew_end_time",
    "monday",
    "tuesday",
    "wednesday",
    "thursday",
    "friday",
    "saturday",
    "sunday",
  ),
) {
  fun withLegacySubjectId(legacySubjectId: String): IntegrityServiceDetailsQueryBuilder {
    validateAlphanumeric(legacySubjectId, "legacy_subject_id")

    if (legacySubjectId.isBlank()) {
      return this
    }

    values.add(legacySubjectId)
    whereClauses.put("legacy_subject_id", "legacy_subject_id" eq legacySubjectId)
    return this
  }

  fun build(): AthenaServiceDetailsQuery = AthenaServiceDetailsQuery(getSQL(), values.toTypedArray())
}
