package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AlcoholMonitoringContactEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AlcoholMonitoringIncidentEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AlcoholMonitoringViolationEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.alcoholMonitoring.AthenaAlcoholMonitoringContactEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.alcoholMonitoring.AthenaAlcoholMonitoringIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.alcoholMonitoring.AthenaAlcoholMonitoringViolationEventDTO

@Service
class AlcoholMonitoringOrderEventsRepository(
  val athenaClient: EmDatastoreClientInterface,
) {

  fun getIncidentEventsList(legacySubjectId: String): List<AthenaAlcoholMonitoringIncidentEventDTO> {
    val incidentEventsQuery = AlcoholMonitoringIncidentEventsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(incidentEventsQuery)

    return AthenaHelper.mapTo<AthenaAlcoholMonitoringIncidentEventDTO>(athenaResponse)
  }

  fun getViolationEventsList(legacySubjectId: String): List<AthenaAlcoholMonitoringViolationEventDTO> {
    val violationEventsQuery = AlcoholMonitoringViolationEventsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(violationEventsQuery)

    return AthenaHelper.mapTo<AthenaAlcoholMonitoringViolationEventDTO>(athenaResponse)
  }

  fun getContactEventsList(legacySubjectId: String): List<AthenaAlcoholMonitoringContactEventDTO> {
    val contactEventsQuery = AlcoholMonitoringContactEventsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(contactEventsQuery)

    return AthenaHelper.mapTo<AthenaAlcoholMonitoringContactEventDTO>(athenaResponse)
  }
}
