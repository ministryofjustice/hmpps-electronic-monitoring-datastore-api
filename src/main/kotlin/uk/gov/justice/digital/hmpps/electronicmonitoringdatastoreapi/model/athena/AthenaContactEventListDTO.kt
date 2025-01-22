package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

data class AthenaContactEventListDTO(
  val pageSize: Int,
  val events: List<AthenaContactEventDTO>,
)