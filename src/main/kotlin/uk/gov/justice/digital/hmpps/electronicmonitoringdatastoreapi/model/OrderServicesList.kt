package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaServicesListDTO

data class OrderServicesList(
  val pageSize: Int,
  val events: List<OrderService>,
) {
  constructor(dto: AthenaServicesListDTO) : this (
    pageSize = dto.pageSize,
    events = dto.services.map { service -> OrderService(service) },
  )
}
