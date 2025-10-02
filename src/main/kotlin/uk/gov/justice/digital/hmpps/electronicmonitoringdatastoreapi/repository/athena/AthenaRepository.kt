package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.athena

import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
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
    val queryExecutionId = athenaClient.getQueryExecutionId(athenaQuery)
    val queryResult = athenaClient.getQueryResult(queryExecutionId, restricted)
    return mapTo(queryResult)
  }

  fun isValid(restricted: Boolean = false): Boolean = athenaClient.getQueryResult(AthenaQuery("SELECT 1", arrayOf()), restricted).hasRows()

  // fun findAll(restricted: Boolean = false): List<T> = this.executeQuery(queryBuilder().findAll(), restricted)
  fun findBy(criteria: Any, restricted: Boolean = false): List<T> = this.executeQuery(queryBuilder().findAll(), restricted)
  fun findByLegacySubjectId(legacySubjectId: String, restricted: Boolean = false): List<T> = this.executeQuery(queryBuilder().findByLegacySubjectId(legacySubjectId), restricted)
  fun getByLegacySubjectId(legacySubjectId: String, restricted: Boolean = false): T = findByLegacySubjectId(legacySubjectId, restricted).first()
}
