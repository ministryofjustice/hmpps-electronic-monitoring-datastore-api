package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

data class AthenaMonitoringEventListDTO(
  val pageSize: Int,
  val events: List<AthenaMonitoringEventDTO>,
)
