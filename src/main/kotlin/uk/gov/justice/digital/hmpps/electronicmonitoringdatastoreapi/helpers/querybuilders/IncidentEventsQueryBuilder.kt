package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.apache.commons.lang3.StringUtils.isAlphanumeric
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventsListQuery

class IncidentEventsQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "incident",
  arrayOf(
    "legacy_subject_id",
    "legacy_order_id",
    "violation_alert_type",
    "violation_alert_date",
    "violation_alert_time",
  ),
) {
  fun withLegacySubjectId(subjectId: String): IncidentEventsQueryBuilder {
    if (!isAlphanumeric(subjectId)) {
      throw IllegalArgumentException("Input contains illegal characters")
    }

    parameters["legacy_subject_id"] = subjectId
    return this
  }

  fun build(): AthenaIncidentEventsListQuery = AthenaIncidentEventsListQuery(getSQL(), parameters.values.toTypedArray())
}
