package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.ContactEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.DocumentListQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.IncidentEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.KeyOrderInformationQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.MonitoringEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SubjectHistoryReportQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSubjectHistoryReportDTO

@Service
class OrderInformationRepository(
  @Autowired val athenaClient: EmDatastoreClientInterface,
  @Value("\${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {
  fun getKeyOrderInformation(orderId: String, role: AthenaRole): AthenaKeyOrderInformationDTO {
    val keyOrderInformationQuery = KeyOrderInformationQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(keyOrderInformationQuery, role)

    val result = AthenaHelper.mapTo<AthenaKeyOrderInformationDTO>(athenaResponse)

    return result.first()
  }

  fun getSubjectHistoryReport(orderId: String, role: AthenaRole): AthenaSubjectHistoryReportDTO {
    val subjectHistoryReportQuery = SubjectHistoryReportQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(subjectHistoryReportQuery, role)

    val result = AthenaHelper.mapTo<AthenaSubjectHistoryReportDTO>(athenaResponse)

    return result.first()
  }

  fun getDocumentList(orderId: String, role: AthenaRole): AthenaDocumentListDTO {
    val documentListQuery = DocumentListQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(documentListQuery, role)

    val result = AthenaHelper.mapTo<AthenaDocumentDTO>(athenaResponse)

    return AthenaDocumentListDTO(
      pageSize = result.size,
      orderDocuments = result,
    )
  }

  fun getMonitoringEventsList(orderId: String, role: AthenaRole): AthenaMonitoringEventListDTO {
    val monitoringEventsQuery = MonitoringEventsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(monitoringEventsQuery, role)

    val result = AthenaHelper.mapTo<AthenaMonitoringEventDTO>(athenaResponse)

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

    val result = AthenaHelper.mapTo<AthenaIncidentEventDTO>(athenaResponse)

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

    val result = AthenaHelper.mapTo<AthenaContactEventDTO>(athenaResponse)

    return AthenaContactEventListDTO(
      pageSize = result.size,
      events = result,
    )
  }
}
