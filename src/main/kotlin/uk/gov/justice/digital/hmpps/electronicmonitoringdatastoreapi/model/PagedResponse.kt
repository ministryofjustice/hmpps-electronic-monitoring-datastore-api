package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

data class PagedResponse<T>(
  val data: List<T>,
  val pageCount: Int = 0,
  val pageNumber: Int = 0,
  val pageSize: Int = 0,
)
