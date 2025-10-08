package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.athena

import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaMapper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.queryBuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.queryBuilders.SqlQueryBuilderBase
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import kotlin.reflect.KClass

abstract class AthenaRepository<T : Any>(
  val athenaClient: EmDatastoreClient,
  val kClass: KClass<T>,
) {
  private val mapper = AthenaMapper(kClass)

  fun mapTo(results: ResultSet): List<T> = mapper.mapTo(results)

  fun queryBuilder(): SqlQueryBuilder = SqlQueryBuilderBase(kClass)

  fun executeQuery(athenaQuery: SqlQueryBuilder, restricted: Boolean = false): List<T> {
    val queryExecutionId = athenaClient.getQueryExecutionId(athenaQuery.build(athenaClient.properties.database))
    val resultSet = athenaClient.getQueryResult(queryExecutionId, restricted)
    return mapTo(resultSet)
  }

  fun isValid(restricted: Boolean = false): Boolean {
    val queryResult = athenaClient.getQueryResult(AthenaQuery("SELECT 1", arrayOf()), restricted)
    return queryResult.hasRows()
  }

  fun startSearch(criteria: OrderSearchCriteria, restricted: Boolean = false): String {
    val athenaQuery = queryBuilder().findBy(criteria).build(athenaClient.properties.database)
    return athenaClient.getQueryExecutionId(athenaQuery, restricted)
  }

  fun getSearchResults(queryExecutionId: String, restricted: Boolean = false): List<T> {
    val resultSet = athenaClient.getQueryResult(queryExecutionId, restricted)
    return mapTo(resultSet)
  }

  fun findByLegacySubjectId(legacySubjectId: String, restricted: Boolean = false): List<T> {
    val athenaQuery = queryBuilder().findByLegacySubjectId(legacySubjectId)
    val resultSet = this.executeQuery(athenaQuery, restricted)
    return resultSet
  }

  fun getByLegacySubjectId(legacySubjectId: String, restricted: Boolean = false): T {
    val resultSet = findByLegacySubjectId(legacySubjectId, restricted)
    return resultSet.first()
  }
}
