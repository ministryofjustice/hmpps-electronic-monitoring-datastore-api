package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaVisitDetailsListQuery
import kotlin.collections.toTypedArray

class VisitDetailsQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "visit_details",
  arrayOf(
    "legacy_subject_id",
    "legacy_order_id",
    "address_1",
    "address_2",
    "address_3",
    "postcode",
    "actual_work_start_date",
    "actual_work_start_time",
    "actual_work_end_date",
    "actual_work_end_time",
    "visit_notes",
    "visit_type",
    "visit_outcome",
  ),
) {
  fun withLegacySubjectId(subjectId: String): VisitDetailsQueryBuilder {
    parameters["legacy_subject_id"] = subjectId
    return this
  }

  fun build(): AthenaVisitDetailsListQuery = AthenaVisitDetailsListQuery(getSQL(), parameters.values.toTypedArray())
}
