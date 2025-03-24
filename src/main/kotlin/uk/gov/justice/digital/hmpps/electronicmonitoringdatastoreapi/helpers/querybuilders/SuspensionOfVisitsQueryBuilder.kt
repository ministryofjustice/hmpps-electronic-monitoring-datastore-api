package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.apache.commons.lang3.StringUtils.isAlphanumeric
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSuspensionOfVisitsListQuery

class SuspensionOfVisitsQueryBuilder(
  override var databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "suspension_of_visits",
  arrayOf(
    "legacy_subject_id",
    "legacy_order_id",
    "suspension_of_visits",
    "suspension_of_visits_requested_date",
    "suspension_of_visits_start_date",
    "suspension_of_visits_start_time",
    "suspension_of_visits_end_date",
  ),
) {
  fun withLegacySubjectId(subjectId: String): SuspensionOfVisitsQueryBuilder {
    if (!isAlphanumeric(subjectId)) {
      throw IllegalArgumentException("Input contains illegal characters")
    }

    parameters["legacy_subject_id"] = subjectId
    return this
  }

  fun build(): AthenaSuspensionOfVisitsListQuery = AthenaSuspensionOfVisitsListQuery(getSQL(), parameters.values.toTypedArray())
}
