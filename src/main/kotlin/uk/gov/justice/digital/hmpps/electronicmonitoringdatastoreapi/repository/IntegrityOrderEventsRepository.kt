package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaMapper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.queryBuilders.SqlQueryBuilderBase
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.athena.AthenaRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.ContactHistory
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.EventHistory
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.Incident
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.Violations

@Service
class IntegrityOrderEventsRepository(
  athenaClient: EmDatastoreClient,
) : AthenaRepository<EventHistory>(athenaClient, EventHistory::class) {
  fun getMonitoringEventsList(legacySubjectId: String, restricted: Boolean): List<EventHistory> = this.executeQuery(
    queryBuilder().findByLegacySubjectId(legacySubjectId),
    restricted,
  )

  fun getIncidentEventsList(legacySubjectId: String, restricted: Boolean): List<Incident> {
    val athenaQuery = SqlQueryBuilderBase(Incident::class).findByLegacySubjectId(legacySubjectId)
    val queryExecutionId = athenaClient.getQueryExecutionId(athenaQuery)
    val queryResult = athenaClient.getQueryResult(queryExecutionId, restricted)
    return AthenaMapper(Incident::class).mapTo(queryResult)
  }

  fun getViolationEventsList(legacySubjectId: String, restricted: Boolean): List<Violations> {
    val athenaQuery = SqlQueryBuilderBase(Violations::class).findByLegacySubjectId(legacySubjectId)
    val queryExecutionId = athenaClient.getQueryExecutionId(athenaQuery, restricted)
    val queryResult = athenaClient.getQueryResult(queryExecutionId, restricted)
    return AthenaMapper(Violations::class).mapTo(queryResult)
  }

  fun getContactEventsList(legacySubjectId: String, restricted: Boolean): List<ContactHistory> {
    val athenaQuery = SqlQueryBuilderBase(ContactHistory::class).findByLegacySubjectId(legacySubjectId)
    val queryExecutionId = athenaClient.getQueryExecutionId(athenaQuery, restricted)
    val queryResult = athenaClient.getQueryResult(queryExecutionId, restricted)
    return AthenaMapper(ContactHistory::class).mapTo(queryResult)
  }
}
