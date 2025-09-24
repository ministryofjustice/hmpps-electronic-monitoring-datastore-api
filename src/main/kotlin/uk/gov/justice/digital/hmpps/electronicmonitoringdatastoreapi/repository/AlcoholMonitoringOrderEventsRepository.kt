package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaAlcoholMonitoringContactEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaAlcoholMonitoringContactEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaAlcoholMonitoringIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaAlcoholMonitoringIncidentEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaAlcoholMonitoringViolationEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaAlcoholMonitoringViolationEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper

@Service
class AlcoholMonitoringOrderEventsRepository(
  athenaClient: EmDatastoreClient,
) : AthenaRepository<AthenaAlcoholMonitoringIncidentEventDTO>(athenaClient) {
  override fun mapTo(results: ResultSet): List<AthenaAlcoholMonitoringIncidentEventDTO> = AthenaHelper.mapTo(results)

  fun getIncidentEventsList(legacySubjectId: String): List<AthenaAlcoholMonitoringIncidentEventDTO> = this.executeQuery(
    AthenaAlcoholMonitoringIncidentEventsQueryBuilder().withLegacySubjectId(legacySubjectId),
  )

  fun getViolationEventsList(legacySubjectId: String): List<AthenaAlcoholMonitoringViolationEventDTO> {
    val athenaQuery = AthenaAlcoholMonitoringViolationEventsQueryBuilder().withLegacySubjectId(legacySubjectId)
    val queryExecutionId = athenaClient.getQueryExecutionId(athenaQuery)
    val queryResult = athenaClient.getQueryResult(queryExecutionId)
    return AthenaHelper.mapTo(queryResult)
  }

  fun getContactEventsList(legacySubjectId: String): List<AthenaAlcoholMonitoringContactEventDTO> {
    val athenaQuery = AthenaAlcoholMonitoringContactEventsQueryBuilder().withLegacySubjectId(legacySubjectId)
    val queryExecutionId = athenaClient.getQueryExecutionId(athenaQuery)
    val queryResult = athenaClient.getQueryResult(queryExecutionId)
    return AthenaHelper.mapTo(queryResult)
  }
}
