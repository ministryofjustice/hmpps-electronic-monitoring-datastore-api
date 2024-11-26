package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client


import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.athena.AthenaClient
import software.amazon.awssdk.services.athena.model.*


class AthenaClientFactory {

  fun acquireCredentials(): DefaultCredentialsProvider {

    val credentialsProvider = DefaultCredentialsProvider.builder().build()

    return credentialsProvider;
  }

  private val builder = AthenaClient.builder()
    .region(Region.EU_WEST_2)
//    .credentialsProvider(ProfileCredentialsProvider.create())
    .credentialsProvider(DefaultCredentialsProvider.builder().build())

  fun athenaClient(credentials: DefaultCredentialsProvider): AthenaClient {
    return AthenaClient.builder()
      .region(Region.EU_WEST_2)
      .credentialsProvider(credentials)
      .build()
  }

}
//
//object ExampleConstants {
//  const val CLIENT_EXECUTION_TIMEOUT = 100000
//  const val ATHENA_OUTPUT_BUCKET = "s3://bucketscott2" // change the Amazon S3 bucket name to match
//
//  // your environment
//  // Demonstrates how to query a table with a comma-separated value (CSV) table.
//  // For information, see
//  // https://docs.aws.amazon.com/athena/latest/ug/work-with-data.html
//  const val ATHENA_SAMPLE_QUERY = "SELECT * FROM scott2;" // change the Query statement to match
//
//  // your environment
//  const val SLEEP_AMOUNT_IN_MS: Long = 1000
//  const val ATHENA_DEFAULT_DATABASE = "mydatabase" // change the database to match your database
//}
//
//class MyAthenaClient {
////  companion object Factory {
////
////    private val builder = AthenaClient.builder()
////      .region(Region.EU_WEST_2)
////      .credentialsProvider(DefaultCredentialsProvider.builder().build())
////
////    fun new(): AthenaClient? = builder.build()
////  }
//
////  private val client: AthenaClient = AthenaClientFactory().athenaClient()
////     The QueryExecutionContext allows us to set the database.
//    private val queryExecutionContext: QueryExecutionContext = QueryExecutionContext.builder()
//      .database(ExampleConstants.ATHENA_DEFAULT_DATABASE)
//      .build()
//    // The result configuration specifies where the results of the query should go.
//    private val resultConfiguration: ResultConfiguration = ResultConfiguration.builder()
//      .outputLocation(ExampleConstants.ATHENA_OUTPUT_BUCKET)
//      .build()
//
//  fun query(querystring: String) {
//    val queryExecutionId: String = submitAthenaQuery(querystring)
//    waitForQueryToComplete(queryExecutionId)
//    processResultRows(queryExecutionId)
////    athenaClient.close()
//  }
//
//  // Submits a sample query to Amazon Athena and returns the execution ID of the
//  // query.
//  private fun submitAthenaQuery(querystring: String): String {
//    try {
//
//      val startQueryExecutionRequest = StartQueryExecutionRequest.builder()
//        .queryString(ExampleConstants.ATHENA_SAMPLE_QUERY)
//        .queryExecutionContext(queryExecutionContext)
//        .resultConfiguration(resultConfiguration)
//        .build()
//
//      val startQueryExecutionResponse = client.startQueryExecution(startQueryExecutionRequest)
//
//      return startQueryExecutionResponse.queryExecutionId()
//
//    } catch (e: AthenaException) {
//      e.printStackTrace()
//      System.exit(1)
//    }
//    return ""
//  }
//
//  // Wait for an Amazon Athena query to complete, fail or to be cancelled.
//  @Throws(InterruptedException::class)
//  private fun waitForQueryToComplete(queryExecutionId: String?) {
//    val getQueryExecutionRequest = GetQueryExecutionRequest.builder()
//      .queryExecutionId(queryExecutionId)
//      .build()
//    var getQueryExecutionResponse: GetQueryExecutionResponse
//    var isQueryStillRunning = true
//    while (isQueryStillRunning) {
//      getQueryExecutionResponse = client.getQueryExecution(getQueryExecutionRequest)
//      val queryState = getQueryExecutionResponse.queryExecution().status().state().toString()
//      if (queryState == QueryExecutionState.FAILED.toString()) {
//        throw RuntimeException(
//          "The Amazon Athena query failed to run with error message: " + getQueryExecutionResponse
//            .queryExecution().status().stateChangeReason(),
//        )
//      } else if (queryState == QueryExecutionState.CANCELLED.toString()) {
//        throw RuntimeException("The Amazon Athena query was cancelled.")
//      } else if (queryState == QueryExecutionState.SUCCEEDED.toString()) {
//        isQueryStillRunning = false
//      } else {
//        // Sleep an amount of time before retrying again.
//        Thread.sleep(ExampleConstants.SLEEP_AMOUNT_IN_MS)
//      }
//      println("The current status is: $queryState")
//    }
//  }
//
//  // This code retrieves the results of a query
//  fun processResultRows(queryExecutionId: String?): String {
//    try {
//      // Max Results can be set but if its not set,
//      // it will choose the maximum page size.
//      val getQueryResultsRequest = GetQueryResultsRequest.builder()
//        .queryExecutionId(queryExecutionId)
//        .build()
//      val getQueryResultsResults = client
//        .getQueryResultsPaginator(getQueryResultsRequest)
//      val sb = StringBuilder()
//      for (result in getQueryResultsResults) {
//        val columnInfoList = result.resultSet().resultSetMetadata().columnInfo()
//        val results = result.resultSet().rows()
//        sb.append(processRow(results, columnInfoList))
//      }
//      return sb.toString()
//    } catch (e: AthenaException) {
//      e.printStackTrace()
//      System.exit(1)
//    }
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
//}