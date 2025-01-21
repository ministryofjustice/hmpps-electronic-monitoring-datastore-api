package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery

// We will instantiate as new for now
@Component
@Profile("!integration")
class AthenaClient : AthenaClientInterface {
  private val outputBucket: String = "s3://emds-dev-athena-query-results-20240917144028307600000004"
  private val sleepLength: Long = 1000
  private val databaseName: String = "test_database"
  private val defaultRole: AthenaRole = AthenaRole.DEV

  private fun startClient(role: AthenaRole): AthenaClient {
    val useLocalCredentials: Boolean = false
    var credentialsProvider: AwsCredentialsProvider = AnonymousCredentialsProvider.create()

    if (useLocalCredentials) {
      credentialsProvider = StaticCredentialsProvider
        .create(
          AwsSessionCredentials.create(
            "access_key_id",
            "secret_key_id",
            "session_token",
          ),
        )
    } else {
      val credentialsProvider = AthenaAssumeRoleService.Companion.getModernisationPlatformCredentialsProvider(role)
    }

    return AthenaClient.builder()
      .region(Region.EU_WEST_2)
      .credentialsProvider(credentialsProvider)
      .build()
  }

  // Initialise a query, wait for completion, and return the ResultSet
  override fun getQueryResult(athenaQuery: AthenaQuery, role: AthenaRole?): ResultSet {
    val athenaClient = startClient(role ?: defaultRole)

    val queryExecutionId = submitAthenaQuery(athenaClient, athenaQuery.queryString)

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
