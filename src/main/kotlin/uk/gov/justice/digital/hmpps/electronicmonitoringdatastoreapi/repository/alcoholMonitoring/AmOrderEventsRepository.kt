package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
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
  @Autowired val athenaClient: EmDatastoreClientInterface,
  @Value("\${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {

  fun getIncidentEventsList(legacySubjectId: String, role: AthenaRole): List<AthenaAmIncidentEventDTO> {
    val incidentEventsQuery = AmIncidentEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(incidentEventsQuery, role)

    return AthenaHelper.Companion.mapTo<AthenaAmIncidentEventDTO>(athenaResponse)
  }

  fun getViolationEventsList(legacySubjectId: String, role: AthenaRole): List<AthenaAmViolationEventDTO> {
    val violationEventsQuery = AmViolationEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(violationEventsQuery, role)

    return AthenaHelper.Companion.mapTo<AthenaAmViolationEventDTO>(athenaResponse)
  }

  fun getContactEventsList(legacySubjectId: String, role: AthenaRole): List<AthenaAmContactEventDTO> {
    val contactEventsQuery = AmContactEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(contactEventsQuery, role)

    return AthenaHelper.Companion.mapTo<AthenaAmContactEventDTO>(athenaResponse)
  }
}
