package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

data class AthenaIncidentEventListDTO(
  val pageSize: Int,
  val events: List<AthenaIncidentEventDTO>,
)
