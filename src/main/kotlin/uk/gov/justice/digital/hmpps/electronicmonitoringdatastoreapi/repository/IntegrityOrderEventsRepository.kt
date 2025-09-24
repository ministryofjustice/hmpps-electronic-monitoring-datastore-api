package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityContactEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityContactEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityIncidentEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityMonitoringEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityMonitoringEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityViolationEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityViolationEventsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper

@Service
class IntegrityOrderEventsRepository(
  athenaClient: EmDatastoreClient,
) : AthenaRepository<AthenaIntegrityMonitoringEventDTO>(athenaClient) {
  override fun mapTo(results: ResultSet): List<AthenaIntegrityMonitoringEventDTO> = AthenaHelper.mapTo(results)

  fun getMonitoringEventsList(legacySubjectId: String, restricted: Boolean): List<AthenaIntegrityMonitoringEventDTO> = this.executeQuery(
    AthenaIntegrityMonitoringEventsQueryBuilder().withLegacySubjectId(legacySubjectId),
    restricted,
  )

  fun getIncidentEventsList(legacySubjectId: String, restricted: Boolean): List<AthenaIntegrityIncidentEventDTO> {
    val athenaQuery = AthenaIntegrityIncidentEventsQueryBuilder().withLegacySubjectId(legacySubjectId)
    val queryExecutionId = athenaClient.getQueryExecutionId(athenaQuery)
    val queryResult = athenaClient.getQueryResult(queryExecutionId, restricted)
    return AthenaHelper.mapTo(queryResult)
  }

  fun getViolationEventsList(legacySubjectId: String, restricted: Boolean): List<AthenaIntegrityViolationEventDTO> {
    val athenaQuery = AthenaIntegrityViolationEventsQueryBuilder().withLegacySubjectId(legacySubjectId)
    val queryExecutionId = athenaClient.getQueryExecutionId(athenaQuery, restricted)
    val queryResult = athenaClient.getQueryResult(queryExecutionId, restricted)
    return AthenaHelper.mapTo(queryResult)
  }

  fun getContactEventsList(legacySubjectId: String, restricted: Boolean): List<AthenaIntegrityContactEventDTO> {
    val athenaQuery = AthenaIntegrityContactEventsQueryBuilder().withLegacySubjectId(legacySubjectId)
    val queryExecutionId = athenaClient.getQueryExecutionId(athenaQuery, restricted)
    val queryResult = athenaClient.getQueryResult(queryExecutionId, restricted)
    return AthenaHelper.mapTo(queryResult)
  }
}
