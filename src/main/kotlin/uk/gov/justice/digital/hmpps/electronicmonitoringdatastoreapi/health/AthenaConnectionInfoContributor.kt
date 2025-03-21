package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.health

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.info.Info
import org.springframework.boot.actuate.info.InfoContributor
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService

@Component
class AthenaConnectionInfoContributor(
  @Autowired val orderService: OrderService,
) : InfoContributor {

  override fun contribute(builder: Info.Builder) {
    val emDatastoreConnection = HashMap<String, Any?>()
    emDatastoreConnection.put("status", if (orderService.checkAvailability()) "UP" else "DOWN")
    builder.withDetail("em-datastore", emDatastoreConnection)
  }
}
