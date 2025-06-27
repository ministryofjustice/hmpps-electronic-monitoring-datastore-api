package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Event
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IncidentEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityMonitoringEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityViolationEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityOrderEventsRepository
import java.time.LocalDateTime

@Service
class IntegrityOrderEventsService(
  @field:Autowired val integrityOrderEventsRepository: IntegrityOrderEventsRepository,
) {
  fun getMonitoringEvents(legacySubjectId: String, restricted: Boolean): List<Event<IntegrityMonitoringEventDetails>> {
    val result = integrityOrderEventsRepository.getMonitoringEventsList(legacySubjectId, restricted)

    return result.map { event ->
      Event(
        legacySubjectId = event.legacySubjectId,
        type = "monitoring",
        dateTime = LocalDateTime.parse("${event.eventDate}T${event.eventTime}"),
        details = IntegrityMonitoringEventDetails(event),
      )
    }
  }

  fun getIncidentEvents(legacySubjectId: String, restricted: Boolean): List<Event<IncidentEventDetails>> {
    val result = integrityOrderEventsRepository.getIncidentEventsList(legacySubjectId, restricted)

    return result.map { event ->
      Event(
        legacySubjectId = event.legacySubjectId,
        type = "incident",
        dateTime = LocalDateTime.parse("${event.violationAlertDate}T${event.violationAlertTime}"),
        details = IncidentEventDetails(event),
      )
    }
  }

  fun getViolationEvents(legacySubjectId: String, restricted: Boolean): List<Event<IntegrityViolationEventDetails>> {
    val result = integrityOrderEventsRepository.getViolationEventsList(legacySubjectId, restricted)

    return result.map { event ->
      Event(
        legacySubjectId = event.legacySubjectId,
        type = "violation",
        dateTime = LocalDateTime.parse("${event.breachDate}T${event.breachTime}"),
        details = IntegrityViolationEventDetails(event),
      )
    }
  }

  fun getContactEvents(legacySubjectId: String, restricted: Boolean): List<Event<IntegrityContactEventDetails>> {
    val result = integrityOrderEventsRepository.getContactEventsList(legacySubjectId, restricted)

    return result.map { event ->
      Event(
        legacySubjectId = event.legacySubjectId,
        type = "contact",
        dateTime = LocalDateTime.parse("${event.contactDate}T${event.contactTime}"),
        details = IntegrityContactEventDetails(event),
      )
    }
  }
}
