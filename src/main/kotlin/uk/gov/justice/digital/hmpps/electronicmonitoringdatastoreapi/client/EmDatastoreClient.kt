package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
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
import software.amazon.awssdk.services.athena.model.StartQueryExecutionResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.AthenaClientException
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder

// We will instantiate as new for now
@Component
@Profile("!integration & !mocking")
class EmDatastoreClient : EmDatastoreClientInterface {
  @field:Value($$"${services.athena-roles.restricted:uninitialised}")
  private val restrictedRole: String = "uninitialised"

  @field:Value($$"${services.athena-roles.general:uninitialised}")
  private val generalRole: String = "uninitialised"

  @field:Value($$"${services.athena.output}")
  private val output: String = "s3://emds-dev-athena-query-results-20240917144028307600000004"
  private val sleepLength: Long = 1000

  @field:Value($$"${services.athena.database}")
  private val databaseName: String = "test_database"

  private fun startClient(iamRole: String): AthenaClient {
    val credentialsProvider: AwsCredentialsProvider = EmDatastoreCredentialsProvider.getCredentials(iamRole)

    return AthenaClient.builder()
      .region(Region.EU_WEST_2)
      .credentialsProvider(credentialsProvider)
      .build()
  }

  override fun getQueryExecutionId(athenaQuery: SqlQueryBuilder, restricted: Boolean): String {
    val iamRole = if (restricted) restrictedRole else generalRole

    val athenaClient = startClient(iamRole)
    val queryExecutionId = submitAthenaQuery(athenaClient, athenaQuery)
    athenaClient.close()
    return queryExecutionId
  }

  override fun getQueryResult(queryExecutionId: String, restricted: Boolean): ResultSet {
    val iamRole = if (restricted) restrictedRole else generalRole

    val athenaClient = startClient(iamRole)

    waitForQueryToComplete(athenaClient, queryExecutionId)
    val resultSet: ResultSet = retrieveResults(athenaClient, queryExecutionId)

    athenaClient.close()
    return resultSet
  }

  override fun getQueryResult(athenaQuery: SqlQueryBuilder, restricted: Boolean): ResultSet {
    val iamRole = if (restricted) restrictedRole else generalRole

    val athenaClient = startClient(iamRole)

    val queryExecutionId: String = submitAthenaQuery(athenaClient, athenaQuery)

    // Wait for query to complete - blocking
    waitForQueryToComplete(athenaClient, queryExecutionId)

    val resultSet: ResultSet = retrieveResults(athenaClient, queryExecutionId)

    athenaClient.close()
    return resultSet
  }

  @Throws(AthenaClientException::class)
  private fun submitAthenaQuery(athenaClient: AthenaClient, athenaQuery: SqlQueryBuilder): String {
    val query = athenaQuery.build(databaseName)

    // The QueryExecutionContext allows us to set the database.
    val queryExecutionContext = QueryExecutionContext.builder()
      .database(databaseName)
      .build()

    // The result configuration specifies where the results of the query should go.
    val resultConfiguration = ResultConfiguration.builder()
      .outputLocation(output)
      .build()

    // TODO: Consider whether to enable the reuse of results - false by default
    //  // result reuse configuration determines whether results should be reused
    //  val resultReuseConfiguration = ResultReuseConfiguration.builder()
    //    .resultReuseByAgeConfiguration(ResultReuseByAgeConfiguration.builder().enabled(false).build())
    //    .build()

    val startQueryExecutionRequest = StartQueryExecutionRequest.builder()
      .queryString(query.queryString)
      .queryExecutionContext(queryExecutionContext)

    if (query.parameters.isNotEmpty()) {
      startQueryExecutionRequest.executionParameters(*query.parameters)
    }

    startQueryExecutionRequest.resultConfiguration(resultConfiguration)
    // TODO: Consider whether to enable the reuse of results - false by default
    // .resultReuseConfiguration(resultReuseConfiguration)

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
          Thread.sleep(sleepLength)
        }
      }

      println("The current status is: $queryState")
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
}
