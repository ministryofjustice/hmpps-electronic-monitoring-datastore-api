package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaMapper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.queryBuilders.SqlQueryBuilderBase
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.athena.AthenaRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.AmContactHistory
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.AmIncident
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.AmViolations

@Service
class AlcoholMonitoringOrderEventsRepository(
  athenaClient: EmDatastoreClient,
) : AthenaRepository<AmIncident>(athenaClient, AmIncident::class) {
  fun getIncidentEventsList(legacySubjectId: String): List<AmIncident> = this.executeQuery(
    queryBuilder().findByLegacySubjectId(legacySubjectId),
  )

  fun getViolationEventsList(legacySubjectId: String): List<AmViolations> {
    val athenaQuery = SqlQueryBuilderBase(AmViolations::class).findByLegacySubjectId(legacySubjectId)
    val queryExecutionId = athenaClient.getQueryExecutionId(athenaQuery)
    val queryResult = athenaClient.getQueryResult(queryExecutionId)
    return AthenaMapper(AmViolations::class).mapTo(queryResult)
  }

  fun getContactEventsList(legacySubjectId: String): List<AmContactHistory> {
    val athenaQuery = SqlQueryBuilderBase(AmContactHistory::class).findByLegacySubjectId(legacySubjectId)
    val queryExecutionId = athenaClient.getQueryExecutionId(athenaQuery)
    val queryResult = athenaClient.getQueryResult(queryExecutionId)
    return AthenaMapper(AmContactHistory::class).mapTo(queryResult)
  }
}
