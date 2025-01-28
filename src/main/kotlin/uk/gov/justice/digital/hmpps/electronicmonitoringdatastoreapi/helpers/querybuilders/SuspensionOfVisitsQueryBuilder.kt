package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSuspensionOfVisitsListQuery

class SuspensionOfVisitsQueryBuilder(
  var databaseName: String? = null,
) {
  var legacySubjectId: String? = null

  fun withLegacySubjectId(subjectId: String): SuspensionOfVisitsQueryBuilder {
    legacySubjectId = subjectId
    return this
  }

  fun build(): AthenaSuspensionOfVisitsListQuery = AthenaSuspensionOfVisitsListQuery(
    """
      SELECT
          legacy_subject_id
        , suspension_of_visits
        , suspension_of_visits_requested_date
        , suspension_of_visits_start_date
        , suspension_of_visits_start_time
        , suspension_of_visits_end_date
      FROM
        $databaseName.suspension_of_visits
      WHERE
        legacy_subject_id = $legacySubjectId
    """.trimIndent(),
  )
}
