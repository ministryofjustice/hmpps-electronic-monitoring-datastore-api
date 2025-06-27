package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AmContactEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AmIncidentEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.alcoholMonitoring.AmViolationEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmContactEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmViolationEventDTO

@Service
class AmOrderEventsRepository(
  val athenaClient: EmDatastoreClientInterface,
) {

  fun getIncidentEventsList(legacySubjectId: String): List<AthenaAmIncidentEventDTO> {
    val incidentEventsQuery = AmIncidentEventsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(incidentEventsQuery)

    return AthenaHelper.Companion.mapTo<AthenaAmIncidentEventDTO>(athenaResponse)
  }

  fun getViolationEventsList(legacySubjectId: String): List<AthenaAmViolationEventDTO> {
    val violationEventsQuery = AmViolationEventsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(violationEventsQuery)

    return AthenaHelper.Companion.mapTo<AthenaAmViolationEventDTO>(athenaResponse)
  }

  fun getContactEventsList(legacySubjectId: String): List<AthenaAmContactEventDTO> {
    val contactEventsQuery = AmContactEventsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(contactEventsQuery)

    return AthenaHelper.Companion.mapTo<AthenaAmContactEventDTO>(athenaResponse)
  }
}
