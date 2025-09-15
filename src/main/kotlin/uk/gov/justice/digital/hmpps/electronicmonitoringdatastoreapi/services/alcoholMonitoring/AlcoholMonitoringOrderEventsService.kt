package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AlcoholMonitoringContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AlcoholMonitoringEvent
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AlcoholMonitoringIncidentEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AlcoholMonitoringViolationEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring.AlcoholMonitoringOrderEventsRepository
import java.time.LocalDateTime

@Service
class AlcoholMonitoringOrderEventsService(
  val orderEventsRepository: AlcoholMonitoringOrderEventsRepository,
) {

  fun getIncidentEvents(legacySubjectId: String): List<AlcoholMonitoringEvent<AlcoholMonitoringIncidentEventDetails>> {
    val result = orderEventsRepository.getIncidentEventsList(legacySubjectId)

    return result.map { event ->
      AlcoholMonitoringEvent(
        legacySubjectId = event.legacySubjectId,
        type = "am-incident",
        dateTime = LocalDateTime.parse("${event.violationAlertDate}T${event.violationAlertTime}"),
        details = AlcoholMonitoringIncidentEventDetails(event),
      )
    }
  }

  fun getViolationEvents(legacySubjectId: String): List<AlcoholMonitoringEvent<AlcoholMonitoringViolationEventDetails>> {
    val result = orderEventsRepository.getViolationEventsList(legacySubjectId)

    return result.map { event ->
      AlcoholMonitoringEvent(
        legacySubjectId = event.legacySubjectId,
        type = "am-violation",
        dateTime = LocalDateTime.parse("${event.nonComplianceDate}T${event.nonComplianceTime}"),
        details = AlcoholMonitoringViolationEventDetails(event),
      )
    }
  }

  fun getContactEvents(legacySubjectId: String): List<AlcoholMonitoringEvent<AlcoholMonitoringContactEventDetails>> {
    val result = orderEventsRepository.getContactEventsList(legacySubjectId)

    return result.map { event ->
      AlcoholMonitoringEvent(
        legacySubjectId = event.legacySubjectId,
        type = "am-contact",
        dateTime = LocalDateTime.parse("${event.contactDate}T${event.contactTime}"),
        details = AlcoholMonitoringContactEventDetails(event),
      )
    }
  }
}
