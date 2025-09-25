package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import software.amazon.awssdk.services.athena.AthenaClient
import software.amazon.awssdk.services.athena.model.AthenaException
import software.amazon.awssdk.services.athena.model.GetQueryExecutionRequest
import software.amazon.awssdk.services.athena.model.GetQueryExecutionResponse
import software.amazon.awssdk.services.athena.model.GetQueryResultsRequest
import software.amazon.awssdk.services.athena.model.GetQueryResultsResponse
import software.amazon.awssdk.services.athena.model.QueryExecutionContext
import software.amazon.awssdk.services.athena.model.QueryExecutionState
import software.amazon.awssdk.services.athena.model.ResultConfiguration
import software.amazon.awssdk.services.athena.model.ResultSet
import software.amazon.awssdk.services.athena.model.StartQueryExecutionRequest
import software.amazon.awssdk.services.athena.model.StartQueryExecutionResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.AthenaClientException
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.datastore.DatastoreProperties
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.SqlQueryBuilder

@Component
@EnableConfigurationProperties(DatastoreProperties::class)
class EmDatastoreClient(
  @field:Qualifier("athenaGeneralClient") val athenaGeneralClient: AthenaClient,
  @field:Qualifier("athenaRestrictedClient") val athenaRestrictedClient: AthenaClient,
  val properties: DatastoreProperties,
) {

  private val log = LoggerFactory.getLogger(this::class.java)

  fun getQueryResult(athenaQuery: SqlQueryBuilder, restricted: Boolean = false): ResultSet {
    val athenaClient = getAthenaClient(restricted)
    val queryExecutionId: String = submitAthenaQuery(athenaClient, athenaQuery)

    // Wait for query to complete - blocking
    waitForQueryToComplete(athenaClient, queryExecutionId)

    val resultSet: ResultSet = retrieveResults(athenaClient, queryExecutionId)
    return resultSet
  }

  fun getQueryResult(queryExecutionId: String, restricted: Boolean = false): ResultSet {
    val athenaClient = getAthenaClient(restricted)
    waitForQueryToComplete(athenaClient, queryExecutionId)
    val resultSet: ResultSet = retrieveResults(athenaClient, queryExecutionId)
    return resultSet
  }

  @Cacheable("athenaQueryExecutions")
  @Transactional
  fun getQueryExecutionId(athenaQuery: SqlQueryBuilder, restricted: Boolean = false): String {
    val athenaClient = getAthenaClient(restricted)
    val queryExecutionId: String = submitAthenaQuery(athenaClient, athenaQuery)
    return queryExecutionId
  }

  @Throws(AthenaClientException::class)
  private fun submitAthenaQuery(athenaClient: AthenaClient, athenaQuery: SqlQueryBuilder): String {
    val query = athenaQuery.build(properties.database)

    val queryExecutionContext = QueryExecutionContext.builder()
      .database(properties.database)
      .build()

    // The result configuration specifies where the results of the query should go.
    val resultConfiguration = ResultConfiguration.builder()
      .outputLocation(properties.outputBucketArn)
      .build()

    /*
    // result reuse configuration determines whether results should be reused
    val resultReuseConfiguration = ResultReuseConfiguration.builder()
      .resultReuseByAgeConfiguration(ResultReuseByAgeConfiguration.builder().enabled(false).build())
      .build()
     */

    val startQueryExecutionRequest = StartQueryExecutionRequest.builder()
      .queryString(query.queryString)
      .queryExecutionContext(queryExecutionContext)

    if (query.parameters.isNotEmpty()) {
      startQueryExecutionRequest.executionParameters(*query.parameters)
    }

    startQueryExecutionRequest.resultConfiguration(resultConfiguration)
    // .resultReuseConfiguration(resultReuseConfiguration)

    log.debug("Starting query: {}", query)

    var startQueryExecutionResponse: StartQueryExecutionResponse
    try {
      startQueryExecutionResponse = athenaClient.startQueryExecution(startQueryExecutionRequest.build())
    } catch (e: AthenaException) {
      throw AthenaClientException("Error submitting query to Athena: ${e.message}")
    }

    return startQueryExecutionResponse.queryExecutionId()
  }

  // Wait for an Amazon Athena query to complete, fail or to be cancelled.
  @Throws(InterruptedException::class)
  private fun waitForQueryToComplete(athenaClient: AthenaClient, queryExecutionId: String?) {
    val getQueryExecutionRequest = GetQueryExecutionRequest.builder()
      .queryExecutionId(queryExecutionId)
      .build()

    var getQueryExecutionResponse: GetQueryExecutionResponse
    var isQueryStillRunning = true

    while (isQueryStillRunning) {
      getQueryExecutionResponse = athenaClient.getQueryExecution(getQueryExecutionRequest)

      val queryState = getQueryExecutionResponse.queryExecution().status().state().toString()

      when (queryState) {
        QueryExecutionState.FAILED.toString() -> {
          throw RuntimeException(
            "The Amazon Athena query failed to run with error message: " + getQueryExecutionResponse
              .queryExecution().status().stateChangeReason(),
          )
        }
        QueryExecutionState.CANCELLED.toString() -> {
          throw RuntimeException("The Amazon Athena query was cancelled.")
        }
        QueryExecutionState.SUCCEEDED.toString() -> {
          isQueryStillRunning = false
        }
        else -> {
          // Sleep an amount of time before retrying again.
          Thread.sleep(properties.retryIntervalMs)
        }
      }

      log.debug("Query execution id $queryExecutionId has status: $queryState")
    }
  }

  @Throws(AthenaClientException::class)
  private fun retrieveResults(athenaClient: AthenaClient, queryExecutionId: String?): ResultSet {
    val getQueryResultsRequest = GetQueryResultsRequest.builder()
      .queryExecutionId(queryExecutionId)
      .build()

    val queryResults: GetQueryResultsResponse
    try {
      queryResults = athenaClient.getQueryResults(getQueryResultsRequest)
    } catch (e: AthenaException) {
      throw AthenaClientException("Error submitting query to Athena: ${e.message}")
    }

    return queryResults.resultSet()
  }

  private fun getAthenaClient(restricted: Boolean) = if (restricted) athenaRestrictedClient else athenaGeneralClient
}
