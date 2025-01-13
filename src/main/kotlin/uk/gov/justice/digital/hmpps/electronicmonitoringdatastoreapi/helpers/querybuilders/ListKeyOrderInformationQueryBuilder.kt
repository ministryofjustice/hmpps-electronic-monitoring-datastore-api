package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchResultDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery

class ListKeyOrderInformationQueryBuilder {
  fun build(): AthenaQuery<AthenaOrderSearchResultDTO> = AthenaQuery(
    """
        SELECT 
          legacy_subject_id
        FROM 
          test_database.order_details
    """.trimIndent(),
  )
}
