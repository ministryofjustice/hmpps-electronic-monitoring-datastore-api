package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.apache.commons.lang3.StringUtils.isAlphanumeric
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventsListQuery

class MonitoringEventsQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "event_history",
  arrayOf(
    "legacy_subject_id",
    "legacy_order_id",
    "event_type",
    "event_date",
    "event_time",
    "event_second",
    "process_date",
    "process_time",
    "process_second",
  ),
) {
  fun withLegacySubjectId(subjectId: String): MonitoringEventsQueryBuilder {
    if (!isAlphanumeric(subjectId)) {
      throw IllegalArgumentException("Input contains illegal characters")
    }

    parameters["legacy_subject_id"] = subjectId
    return this
  }

  fun build(): AthenaMonitoringEventsListQuery = AthenaMonitoringEventsListQuery(getSQL(), parameters.values.toTypedArray())
}
