package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.ContactEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.IncidentEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.MonitoringEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventDTO

@Service
class OrderEventsRepository(
  @Autowired val athenaClient: EmDatastoreClientInterface,
  @Value("\${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {
  fun getMonitoringEventsList(orderId: String, role: AthenaRole): List<AthenaMonitoringEventDTO> {
    val monitoringEventsQuery = MonitoringEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(monitoringEventsQuery, role)

    return AthenaHelper.Companion.mapTo<AthenaMonitoringEventDTO>(athenaResponse)
  }

  fun getIncidentEventsList(orderId: String, role: AthenaRole): List<AthenaIncidentEventDTO> {
    val incidentEventsQuery = IncidentEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(incidentEventsQuery, role)

    return AthenaHelper.Companion.mapTo<AthenaIncidentEventDTO>(athenaResponse)
  }

  fun getContactEventsList(orderId: String, role: AthenaRole): List<AthenaContactEventDTO> {
    val contactEventsQuery = ContactEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(contactEventsQuery, role)

    return AthenaHelper.Companion.mapTo<AthenaContactEventDTO>(athenaResponse)
  }
}
