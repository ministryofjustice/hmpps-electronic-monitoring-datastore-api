package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaClientFactory
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.AthenaClientException

// We will instantiate as new for now
class AthenaService {

  private val clientFactory = AthenaClientFactory()
  private val stsService = AssumeRoleService()

  object ExampleConstants {
    const val CLIENT_EXECUTION_TIMEOUT = 1000
    const val ATHENA_OUTPUT_BUCKET = "s3://emds-dev-athena-query-results-20240917144028307600000004" // change the Amazon S3 bucket name to match

    // your environment
    // Demonstrates how to query a table with a comma-separated value (CSV) table.
    // For information, see
    // https://docs.aws.amazon.com/athena/latest/ug/work-with-data.html
    const val QUERY_EXAMPLE = "SELECT * FROM dummy_table_1 limit 10;" // change the Query statement to match

    // your environment
    const val SLEEP_AMOUNT_IN_MS: Long = 1000
    const val ATHENA_DEFAULT_DATABASE = "test_database" // change the database to match your database
  }

  fun getQueryResult(role: AthenaRole, querystring: String): ResultSet {
    val modernisationPlatformCredentialsProvider = stsService.getModernisationPlatformCredentialsProvider(role)

    val athenaClient = AthenaClient.builder()
      .region(Region.EU_WEST_2)
      .credentialsProvider(modernisationPlatformCredentialsProvider)
      .build()

    val queryExecutionId = submitAthenaQuery(athenaClient, querystring)

    // Wait for query to complete - blocking
    waitForQueryToComplete(athenaClient, queryExecutionId)

    val output: ResultSet = retrieveResults(athenaClient, queryExecutionId)

    athenaClient.close()
    return output
  }

  // Submits a sample query to Amazon Athena and returns the execution ID of the
  // query.
  @Throws(AthenaClientException::class)
  private fun submitAthenaQuery(athenaClient: AthenaClient, querystring: String): String {
    return try {
      // The QueryExecutionContext allows us to set the database.
      val queryExecutionContext = QueryExecutionContext.builder()
        .database(ExampleConstants.ATHENA_DEFAULT_DATABASE)
        .build()

      // The result configuration specifies where the results of the query should go.
      val resultConfiguration = ResultConfiguration.builder()
        .outputLocation(ExampleConstants.ATHENA_OUTPUT_BUCKET)
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
        Thread.sleep(ExampleConstants.SLEEP_AMOUNT_IN_MS)
      }
      println("The current status is: $queryState")
    }
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

//  // This code retrieves the results of a query
//  fun processResultRows(athenaClient: AthenaClient, queryExecutionId: String?): String {
//    val sb = StringBuilder()
//
//    try {
//      // Max Results can be set but if its not set,
//      // it will choose the maximum page size.
//      val getQueryResultsRequest = GetQueryResultsRequest.builder()
//        .queryExecutionId(queryExecutionId)
//        .build()
//      val getQueryResultsResults = athenaClient
//        .getQueryResultsPaginator(getQueryResultsRequest)
//      for (result in getQueryResultsResults) {
//        val columnInfoList = result.resultSet().resultSetMetadata().columnInfo()
//        val results = result.resultSet().rows()
//        sb.append(processRow(results, columnInfoList))
//      }
//    } catch (e: AthenaException) {
//      e.printStackTrace()
//      System.exit(1)
//    }
//
//    return sb.toString()
//  }
//
//  private fun processRow(row: List<Row>, columnInfoList: List<ColumnInfo>): String {
//    val sb = StringBuilder()
//
//    for (myRow in row) {
//      val allData = myRow.data()
//      for (data in allData) {
//        sb.append("The value of the column is " + data.varCharValue())
//      }
//    }
//
//    return sb.toString()
//  }
}
