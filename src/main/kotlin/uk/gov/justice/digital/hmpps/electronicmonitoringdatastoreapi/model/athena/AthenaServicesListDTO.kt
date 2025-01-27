package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

data class AthenaServicesListDTO(
  val pageSize: Int,
  val services: List<AthenaServicesDTO>,
)
