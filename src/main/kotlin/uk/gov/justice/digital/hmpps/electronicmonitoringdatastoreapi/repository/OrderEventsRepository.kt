package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.ContactEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.IncidentEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.MonitoringEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.ViolationEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaViolationEventDTO

@Service
class OrderEventsRepository(
  @Autowired val athenaClient: EmDatastoreClientInterface,
  @Value("\${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {
  fun getMonitoringEventsList(legacySubjectId: String, allowSpecials: Boolean): List<AthenaMonitoringEventDTO> {
    val monitoringEventsQuery = MonitoringEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(monitoringEventsQuery, allowSpecials)

    return AthenaHelper.Companion.mapTo<AthenaMonitoringEventDTO>(athenaResponse)
  }

  fun getIncidentEventsList(legacySubjectId: String, allowSpecials: Boolean): List<AthenaIncidentEventDTO> {
    val incidentEventsQuery = IncidentEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(incidentEventsQuery, allowSpecials)

    return AthenaHelper.Companion.mapTo<AthenaIncidentEventDTO>(athenaResponse)
  }

  fun getViolationEventsList(legacySubjectId: String, allowSpecials: Boolean): List<AthenaViolationEventDTO> {
    val violationEventsQuery = ViolationEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(violationEventsQuery, allowSpecials)

    return AthenaHelper.Companion.mapTo<AthenaViolationEventDTO>(athenaResponse)
  }

  fun getContactEventsList(legacySubjectId: String, allowSpecials: Boolean): List<AthenaContactEventDTO> {
    val contactEventsQuery = ContactEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(contactEventsQuery, allowSpecials)

    return AthenaHelper.Companion.mapTo<AthenaContactEventDTO>(athenaResponse)
  }
}
