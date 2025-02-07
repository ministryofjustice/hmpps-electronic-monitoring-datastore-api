package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.ContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Event
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.IncidentEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.MonitoringEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.ViolationEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderEventsRepository
import java.time.LocalDateTime

@Service
class OrderEventsService(
  @Autowired val orderEventsRepository: OrderEventsRepository,
) {
  fun getMonitoringEvents(orderId: String, role: AthenaRole): List<Event<MonitoringEventDetails>> {
    val result = orderEventsRepository.getMonitoringEventsList(orderId, role)

    return result.map { event ->
      Event<MonitoringEventDetails>(
        legacyOrderId = event.legacyOrderId,
        legacySubjectId = event.legacySubjectId,
        type = event.eventType,
        dateTime = LocalDateTime.parse("${event.eventDate}T${event.eventTime}"),
        details = MonitoringEventDetails(event),
      )
    }
  }

  fun getIncidentEvents(orderId: String, role: AthenaRole): List<Event<IncidentEventDetails>> {
    val result = orderEventsRepository.getIncidentEventsList(orderId, role)

    return result.map { event ->
      Event<IncidentEventDetails>(
        legacyOrderId = event.legacyOrderId,
        legacySubjectId = event.legacySubjectId,
        type = event.violationAlertType,
        dateTime = LocalDateTime.parse("${event.violationAlertDate}T${event.violationAlertTime}"),
        details = IncidentEventDetails(event),
      )
    }
  }

  fun getViolationEvents(orderId: String, role: AthenaRole): List<Event<ViolationEventDetails>> {
    val result = orderEventsRepository.getViolationEventsList(orderId, role)

    return result.map { event ->
      Event<ViolationEventDetails>(
        legacyOrderId = event.legacyOrderId,
        legacySubjectId = event.legacySubjectId,
        type = "violation",
        dateTime = LocalDateTime.parse("${event.breachDate}T${event.breachTime}"),
        details = ViolationEventDetails(event),
      )
    }
  }

  fun getContactEvents(orderId: String, role: AthenaRole): List<Event<ContactEventDetails>> {
    val result = orderEventsRepository.getContactEventsList(orderId, role)

    return result.map { event ->
      Event<ContactEventDetails>(
        legacyOrderId = event.legacyOrderId,
        legacySubjectId = event.legacySubjectId,
        type = event.contactType,
        dateTime = LocalDateTime.parse("${event.contactDate}T${event.contactTime}"),
        details = ContactEventDetails(event),
      )
    }
  }
}
