package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchQuery

class ListKeyOrderInformationQueryBuilder {
  fun build(): AthenaOrderSearchQuery = AthenaOrderSearchQuery(
    """
        SELECT 
          legacy_subject_id
        FROM 
          test_database.order_details
    """.trimIndent(),
  )
}
