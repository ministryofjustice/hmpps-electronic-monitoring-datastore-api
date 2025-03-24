package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.apache.commons.lang3.StringUtils.isAlphanumeric
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventsListQuery

class ContactEventsQueryBuilder(
  override val databaseName: String,
) : SqlQueryBuilder(
  databaseName,
  "contact_history",
  arrayOf(
    "legacy_subject_id",
    "legacy_order_id",
    "outcome",
    "contact_type",
    "reason",
    "channel",
    "user_id",
    "user_name",
    "contact_date",
    "contact_time",
    "modified_date",
    "modified_time",
  ),
) {
  fun withLegacySubjectId(subjectId: String): ContactEventsQueryBuilder {
    if (!isAlphanumeric(subjectId)) {
      throw IllegalArgumentException("Input contains illegal characters")
    }

    parameters["legacy_subject_id"] = subjectId
    return this
  }

  fun build(): AthenaContactEventsListQuery = AthenaContactEventsListQuery(getSQL(), parameters.values.toTypedArray())
}
