package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.ContactEventList
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.IncidentEventList
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.MonitoringEventList
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderEventsRepository

@Service
class OrderEventsService(
  @Autowired val orderEventsRepository: OrderEventsRepository,
) {
  fun getMonitoringEvents(orderId: String, role: AthenaRole): MonitoringEventList {
    val result = orderEventsRepository.getMonitoringEventsList(orderId, role)

    return MonitoringEventList(result)
  }

  fun getIncidentEvents(orderId: String, role: AthenaRole): IncidentEventList {
    val result = orderEventsRepository.getIncidentEventsList(orderId, role)

    return IncidentEventList(result)
  }

  fun getContactEvents(orderId: String, role: AthenaRole): ContactEventList {
    val result = orderEventsRepository.getContactEventsList(orderId, role)

    return ContactEventList(result)
  }
}