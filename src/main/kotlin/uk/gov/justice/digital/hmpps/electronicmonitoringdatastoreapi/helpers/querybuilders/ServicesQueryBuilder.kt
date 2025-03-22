package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaServicesQuery

class ServicesQueryBuilder(
  var databaseName: String? = null,
) {
  var legacySubjectId: String? = null

  fun withLegacySubjectId(subjectId: String): ServicesQueryBuilder {
    legacySubjectId = subjectId
    return this
  }

  fun build(): AthenaServicesQuery = AthenaServicesQuery(
    """
      SELECT
          legacy_subject_id
        , service_id
        , service_address_1
        , service_address_2
        , service_address_3
        , service_address_postcode
        , service_start_date
        , service_end_date
        , curfew_start_date
        , curfew_end_date
        , curfew_start_time
        , curfew_end_time
        , monday
        , tuesday
        , wednesday
        , thursday
        , friday
        , saturday
        , sunday
      FROM
        $databaseName.services
      WHERE
        legacy_subject_id = $legacySubjectId
    """.trimIndent(),
    arrayOf(),
  )
}
