package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityContactEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityIncidentEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityMonitoringEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityViolationEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityContactEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityMonitoringEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityViolationEventDTO

@Service
class IntegrityOrderEventsRepository(
  @Autowired val athenaClient: EmDatastoreClientInterface,
  @Value("\${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {
  fun getMonitoringEventsList(legacySubjectId: String, role: AthenaRole): List<AthenaIntegrityMonitoringEventDTO> {
    val monitoringEventsQuery = IntegrityMonitoringEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(monitoringEventsQuery, role)

    return AthenaHelper.Companion.mapTo<AthenaIntegrityMonitoringEventDTO>(athenaResponse)
  }

  fun getIncidentEventsList(legacySubjectId: String, role: AthenaRole): List<AthenaIntegrityIncidentEventDTO> {
    val incidentEventsQuery = IntegrityIncidentEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(incidentEventsQuery, role)

    return AthenaHelper.Companion.mapTo<AthenaIntegrityIncidentEventDTO>(athenaResponse)
  }

  fun getViolationEventsList(legacySubjectId: String, role: AthenaRole): List<AthenaIntegrityViolationEventDTO> {
    val violationEventsQuery = IntegrityViolationEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(violationEventsQuery, role)

    return AthenaHelper.Companion.mapTo<AthenaIntegrityViolationEventDTO>(athenaResponse)
  }

  fun getContactEventsList(legacySubjectId: String, restricted: Boolean): List<AthenaIntegrityContactEventDTO> {
    val contactEventsQuery = IntegrityContactEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(contactEventsQuery, restricted)

    return AthenaHelper.Companion.mapTo<AthenaIntegrityContactEventDTO>(athenaResponse)
  }
}
