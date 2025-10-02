package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.IntegrityContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.IntegrityEvent
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.IntegrityIncidentEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.IntegrityMonitoringEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.IntegrityViolationEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.IntegrityOrderEventsRepository
import java.time.LocalDateTime

@Service
class IntegrityOrderEventsService(
  @field:Autowired val integrityOrderEventsRepository: IntegrityOrderEventsRepository,
) {
  fun getMonitoringEvents(legacySubjectId: String, restricted: Boolean): List<IntegrityEvent<IntegrityMonitoringEventDetails>> {
    val result = integrityOrderEventsRepository.getMonitoringEventsList(legacySubjectId, restricted)

    return result.map { event ->
      IntegrityEvent(
        legacySubjectId = event.legacySubjectId,
        type = "monitoring",
        dateTime = LocalDateTime.parse("${event.eventDate}T${event.eventTime}"),
        details = IntegrityMonitoringEventDetails(event),
      )
    }
  }

  fun getIncidentEvents(legacySubjectId: String, restricted: Boolean): List<IntegrityEvent<IntegrityIncidentEventDetails>> {
    val result = integrityOrderEventsRepository.getIncidentEventsList(legacySubjectId, restricted)

    return result.map { event ->
      IntegrityEvent(
        legacySubjectId = event.legacySubjectId,
        type = "incident",
        dateTime = LocalDateTime.parse("${event.violationAlertDate}T${event.violationAlertTime}"),
        details = IntegrityIncidentEventDetails(event),
      )
    }
  }

  fun getViolationEvents(legacySubjectId: String, restricted: Boolean): List<IntegrityEvent<IntegrityViolationEventDetails>> {
    val result = integrityOrderEventsRepository.getViolationEventsList(legacySubjectId, restricted)

    return result.map { event ->
      IntegrityEvent(
        legacySubjectId = event.legacySubjectId,
        type = "violation",
        dateTime = LocalDateTime.parse("${event.breachDate}T${event.breachTime}"),
        details = IntegrityViolationEventDetails(event),
      )
    }
  }

  fun getContactEvents(legacySubjectId: String, restricted: Boolean): List<IntegrityEvent<IntegrityContactEventDetails>> {
    val result = integrityOrderEventsRepository.getContactEventsList(legacySubjectId, restricted)

    return result.map { event ->
      IntegrityEvent(
        legacySubjectId = event.legacySubjectId,
        type = "contact",
        dateTime = LocalDateTime.parse("${event.contactDate}T${event.contactTime}"),
        details = IntegrityContactEventDetails(event),
      )
    }
  }
}
