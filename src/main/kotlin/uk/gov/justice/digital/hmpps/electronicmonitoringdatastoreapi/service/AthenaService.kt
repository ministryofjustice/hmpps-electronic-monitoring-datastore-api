package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import software.amazon.awssdk.regions.Region
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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.AthenaClientException

// We will instantiate as new for now
class AthenaService {
  private val stsService = AssumeRoleService()
  private val outputBucket: String = "s3://emds-dev-athena-query-results-20240917144028307600000004"
  private val sleepLength: Long = 1000
  private val databaseName: String = "test_database"
  private val defaultRole: AthenaRole = AthenaRole.DEV
//  const val CLIENT_EXECUTION_TIMEOUT = 1000 // TODO: Remove unused constant

  companion object {
    inline fun <reified T> mapTo(resultSet: ResultSet): List<T> {
      val mapper = jacksonObjectMapper()
        .registerKotlinModule()
        .apply {
          propertyNamingStrategy = com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE
        }

      val columnNames: List<String> = resultSet.resultSetMetadata().columnInfo().map { it.name() }

      val mappedRows: List<Map<String, String?>> = resultSet.rows().drop(1).map { row ->
        row.data().mapIndexed { i, datum ->
          columnNames[i] to datum.varCharValue()
        }.toMap()
      }

      return mappedRows.map { row ->
        mapper.convertValue(row, T::class.java)
      }
    }
  }

  private fun startClient(role: AthenaRole): AthenaClient {
    val modernisationPlatformCredentialsProvider = stsService.getModernisationPlatformCredentialsProvider(role)

    return AthenaClient.builder()
      .region(Region.EU_WEST_2)
      .credentialsProvider(modernisationPlatformCredentialsProvider)
      .build()
  }

  // Initialise a query, wait for completion, and return the ResultSet
  fun getQueryResult(role: AthenaRole = defaultRole, querystring: String): ResultSet {
    val athenaClient = startClient(role)

    val queryExecutionId = submitAthenaQuery(athenaClient, querystring)

    // Wait for query to complete - blocking
    waitForQueryToComplete(athenaClient, queryExecutionId)

    val output: ResultSet = retrieveResults(athenaClient, queryExecutionId)

    athenaClient.close()
    return output
  }

  // Submits a query to Amazon Athena and returns the execution ID
  @Throws(AthenaClientException::class)
  fun startQuery(role: AthenaRole = defaultRole, querystring: String): String {
    val athenaClient = startClient(role)
    val queryId: String = submitAthenaQuery(athenaClient, querystring)
    athenaClient.close()
    return queryId
  }

  @Throws(AthenaClientException::class)
  private fun submitAthenaQuery(athenaClient: AthenaClient, querystring: String): String {
    return try {
      // The QueryExecutionContext allows us to set the database.
      val queryExecutionContext = QueryExecutionContext.builder()
        .database(databaseName)
        .build()

      // The result configuration specifies where the results of the query should go.
      val resultConfiguration = ResultConfiguration.builder()
        .outputLocation(outputBucket)
        .build()

      val startQueryExecutionRequest = StartQueryExecutionRequest.builder()
        .queryString(querystring)
        .queryExecutionContext(queryExecutionContext)
        .resultConfiguration(resultConfiguration)
        .build()

      val startQueryExecutionResponse = athenaClient
        .startQueryExecution(startQueryExecutionRequest)

      return startQueryExecutionResponse.queryExecutionId()
    } catch (e: AthenaException) {
      throw AthenaClientException("Error submitting query to Athena: ${e.message}")
//      e.printStackTrace()
//      System.exit(1)
    }
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
      if (queryState == QueryExecutionState.FAILED.toString()) {
        throw RuntimeException(
          "The Amazon Athena query failed to run with error message: " + getQueryExecutionResponse
            .queryExecution().status().stateChangeReason(),
        )
      } else if (queryState == QueryExecutionState.CANCELLED.toString()) {
        throw RuntimeException("The Amazon Athena query was cancelled.")
      } else if (queryState == QueryExecutionState.SUCCEEDED.toString()) {
        isQueryStillRunning = false
      } else {
        // Sleep an amount of time before retrying again.
        Thread.sleep(sleepLength)
      }
      println("The current status is: $queryState")
    }
  }

  // Retrieve the results of a query by execution ID (non-paged)
  @Throws(AthenaClientException::class)
  private fun retrieveResults(role: AthenaRole = defaultRole, queryExecutionId: String?): ResultSet {
    val athenaClient = startClient(role)
    val results: ResultSet = retrieveResults(athenaClient, queryExecutionId)
    athenaClient.close()
    return results
  }

  @Throws(AthenaClientException::class)
  private fun retrieveResults(athenaClient: AthenaClient, queryExecutionId: String?): ResultSet {
    return try {
      val getQueryResultsRequest = GetQueryResultsRequest.builder()
        .queryExecutionId(queryExecutionId)
        .build()

      val queryResults: GetQueryResultsResponse = athenaClient.getQueryResults(getQueryResultsRequest)
      return queryResults.resultSet()
    } catch (e: AthenaException) {
      throw AthenaClientException("Error submitting query to Athena: ${e.message}")
      // e.printStackTrace()
      throw e
    }
  }
}
