package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.integrity

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityContactEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityIncidentEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityMonitoringEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityViolationEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.integrity.AthenaIntegrityContactEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.integrity.AthenaIntegrityIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.integrity.AthenaIntegrityMonitoringEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.integrity.AthenaIntegrityViolationEventDTO

@Service
class IntegrityOrderEventsRepository(
  val athenaClient: EmDatastoreClientInterface,
) {
  fun getMonitoringEventsList(legacySubjectId: String, restricted: Boolean): List<AthenaIntegrityMonitoringEventDTO> {
    val monitoringEventsQuery = IntegrityMonitoringEventsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(monitoringEventsQuery, restricted)

    return AthenaHelper.mapTo<AthenaIntegrityMonitoringEventDTO>(athenaResponse)
  }

  fun getIncidentEventsList(legacySubjectId: String, restricted: Boolean): List<AthenaIntegrityIncidentEventDTO> {
    val incidentEventsQuery = IntegrityIncidentEventsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(incidentEventsQuery, restricted)

    return AthenaHelper.mapTo<AthenaIntegrityIncidentEventDTO>(athenaResponse)
  }

  fun getViolationEventsList(legacySubjectId: String, restricted: Boolean): List<AthenaIntegrityViolationEventDTO> {
    val violationEventsQuery = IntegrityViolationEventsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(violationEventsQuery, restricted)

    return AthenaHelper.mapTo<AthenaIntegrityViolationEventDTO>(athenaResponse)
  }

  fun getContactEventsList(legacySubjectId: String, restricted: Boolean): List<AthenaIntegrityContactEventDTO> {
    val contactEventsQuery = IntegrityContactEventsQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(contactEventsQuery, restricted)

    return AthenaHelper.mapTo<AthenaIntegrityContactEventDTO>(athenaResponse)
  }
}
