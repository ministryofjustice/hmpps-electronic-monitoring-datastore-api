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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventListDTO

@Service
class OrderEventsRepository(
  @Autowired val athenaClient: EmDatastoreClientInterface,
  @Value("\${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {
  fun getMonitoringEventsList(orderId: String, role: AthenaRole): AthenaMonitoringEventListDTO {
    val monitoringEventsQuery = MonitoringEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(monitoringEventsQuery, role)

    val result = AthenaHelper.Companion.mapTo<AthenaMonitoringEventDTO>(athenaResponse)

    return AthenaMonitoringEventListDTO(
      pageSize = result.size,
      events = result,
    )
  }

  fun getIncidentEventsList(orderId: String, role: AthenaRole): AthenaIncidentEventListDTO {
    val incidentEventsQuery = IncidentEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(incidentEventsQuery, role)

    val result = AthenaHelper.Companion.mapTo<AthenaIncidentEventDTO>(athenaResponse)

    return AthenaIncidentEventListDTO(
      pageSize = result.size,
      events = result,
    )
  }

  fun getContactEventsList(orderId: String, role: AthenaRole): AthenaContactEventListDTO {
    val contactEventsQuery = ContactEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(contactEventsQuery, role)

    val result = AthenaHelper.Companion.mapTo<AthenaContactEventDTO>(athenaResponse)

    return AthenaContactEventListDTO(
      pageSize = result.size,
      events = result,
    )
  }
}