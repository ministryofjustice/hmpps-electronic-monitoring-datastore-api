package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AmContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AmEvent
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AmIncidentEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AmViolationEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring.AmOrderEventsRepository
import java.time.LocalDateTime

@Service
class AmOrderEventsService(
  val orderEventsRepository: AmOrderEventsRepository,
) {

  fun getIncidentEvents(legacySubjectId: String): List<AmEvent<AmIncidentEventDetails>> {
    val result = orderEventsRepository.getIncidentEventsList(legacySubjectId)

    return result.map { event ->
      AmEvent(
        legacySubjectId = event.legacySubjectId,
        type = "am-incident",
        dateTime = LocalDateTime.parse("${event.violationAlertDate}T${event.violationAlertTime}"),
        details = AmIncidentEventDetails(event),
      )
    }
  }

  fun getViolationEvents(legacySubjectId: String): List<AmEvent<AmViolationEventDetails>> {
    val result = orderEventsRepository.getViolationEventsList(legacySubjectId)

    return result.map { event ->
      AmEvent(
        legacySubjectId = event.legacySubjectId,
        type = "am-violation",
        dateTime = LocalDateTime.parse("${event.nonComplianceDate}T${event.nonComplianceTime}"),
        details = AmViolationEventDetails(event),
      )
    }
  }

  fun getContactEvents(legacySubjectId: String): List<AmEvent<AmContactEventDetails>> {
    val result = orderEventsRepository.getContactEventsList(legacySubjectId)

    return result.map { event ->
      AmEvent(
        legacySubjectId = event.legacySubjectId,
        type = "am-contact",
        dateTime = LocalDateTime.parse("${event.contactDate}T${event.contactTime}"),
        details = AmContactEventDetails(event),
      )
    }
  }
}
