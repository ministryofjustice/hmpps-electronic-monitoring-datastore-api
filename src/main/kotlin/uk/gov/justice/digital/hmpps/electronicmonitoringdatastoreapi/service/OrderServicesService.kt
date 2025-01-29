package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderServicesRepository

@Service
class OrderServicesService(
  @Autowired val orderServicesRepository: OrderServicesRepository,
) {
  fun getServices(orderId: String, role: AthenaRole): List<OrderService> {
    val result = orderServicesRepository.getServicesList(orderId, role)

    return result.items.map { item -> OrderService(item) }
  }
}
