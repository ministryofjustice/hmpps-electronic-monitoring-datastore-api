package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Event
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmIncidentEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmViolationEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmOrderEventsRepository
import java.time.LocalDateTime

@Service
class AmOrderEventsService(
  @Autowired val orderEventsRepository: AmOrderEventsRepository,
) {

  fun getIncidentEvents(legacySubjectId: String, role: AthenaRole): List<Event<AmIncidentEventDetails>> {
    val result = orderEventsRepository.getIncidentEventsList(legacySubjectId, role)

    return result.map { event ->
      Event<AmIncidentEventDetails>(
        legacyOrderId = event.legacyOrderId.toString(),
        legacySubjectId = event.legacySubjectId.toString(),
        type = "incident",
        dateTime = LocalDateTime.parse("${event.violationAlertDate}T${event.violationAlertTime}"),
        details = AmIncidentEventDetails(event),
      )
    }
  }

  fun getViolationEvents(legacySubjectId: String, role: AthenaRole): List<Event<AmViolationEventDetails>> {
    val result = orderEventsRepository.getViolationEventsList(legacySubjectId, role)

    return result.map { event ->
      Event<AmViolationEventDetails>(
        legacyOrderId = event.legacyOrderId.toString(),
        legacySubjectId = event.legacySubjectId.toString(),
        type = "violation",
        dateTime = LocalDateTime.parse("${event.nonComplianceDate}T${event.nonComplianceTime}"),
        details = AmViolationEventDetails(event),
      )
    }
  }

  fun getContactEvents(legacySubjectId: String, role: AthenaRole): List<Event<AmContactEventDetails>> {
    val result = orderEventsRepository.getContactEventsList(legacySubjectId, role)

    return result.map { event ->
      Event<AmContactEventDetails>(
        legacyOrderId = event.legacyOrderId.toString(),
        legacySubjectId = event.legacySubjectId.toString(),
        type = "contact",
        dateTime = LocalDateTime.parse("${event.contactDate}T${event.contactTime}"),
        details = AmContactEventDetails(event),
      )
    }
  }
}
