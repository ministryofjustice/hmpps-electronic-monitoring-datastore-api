package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

data class AthenaResultListDTO<T>(
  val pageSize: Int,
  val items: List<T>,
)
