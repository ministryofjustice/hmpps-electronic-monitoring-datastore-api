package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchQuery

class ListKeyOrderInformationQueryBuilder(
  var databaseName: String? = null,
) {
  fun build(): AthenaOrderSearchQuery = AthenaOrderSearchQuery(
    """
        SELECT 
          legacy_subject_id
        FROM 
          $databaseName.order_details
    """.trimIndent(),
  )
}
