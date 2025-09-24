package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.SqlQueryBuilderBase

data class Availability(
  @Suppress("PropertyName")
  val `'1'`: String,
)

class AthenaAvailabilityQueryBuilder :
  SqlQueryBuilderBase<Availability>(
    "order_details",
    Availability::class,
  )
