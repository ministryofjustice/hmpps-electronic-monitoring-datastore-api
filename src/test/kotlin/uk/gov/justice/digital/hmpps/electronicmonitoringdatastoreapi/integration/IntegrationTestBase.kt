package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration

import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.stubbing.ServeEvent
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_GENERAL__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.wiremock.AwsApiExtension
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.wiremock.AwsApiExtension.Companion.awsMockServer
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.wiremock.HmppsAuthApiExtension
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.wiremock.HmppsAuthApiExtension.Companion.hmppsAuth
import uk.gov.justice.hmpps.test.kotlin.auth.JwtAuthorisationHelper

@ExtendWith(HmppsAuthApiExtension::class, AwsApiExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
abstract class IntegrationTestBase {

  @BeforeEach
  fun setupBase() {
    awsMockServer.stubStsAssumeRole()
  }

  @AfterEach
  fun teardown() {
    awsMockServer.resetAll()
  }

  @Autowired
  protected lateinit var webTestClient: WebTestClient

  @Autowired
  protected lateinit var jwtAuthHelper: JwtAuthorisationHelper

  internal fun setAuthorisation(
    username: String? = "AUTH_ADM",
    roles: List<String> = listOf(ROLE_EM_DATASTORE_GENERAL__RO),
    scopes: List<String> = listOf("read"),
  ): (HttpHeaders) -> Unit = jwtAuthHelper.setAuthorisationHeader(username = username, scope = scopes, roles = roles)

  protected fun stubPingWithResponse(status: Int) {
    hmppsAuth.stubHealthPing(status)
  }

  protected fun stubGetQueryExecutionId(
    queryExecutionId: String,
    retryCount: Int,
    finalQueryExecutionStatus: String,
  ) {
    awsMockServer.stubAthenaStartQueryExecution(queryExecutionId)
    awsMockServer.stubAthenaGetQueryExecution(retryCount, finalQueryExecutionStatus)
  }

  protected fun stubQueryExecution(
    queryExecutionId: String,
    retryCount: Int,
    finalQueryExecutionStatus: String,
    queryResults: String,
  ) {
    awsMockServer.stubAthenaStartQueryExecution(queryExecutionId)
    awsMockServer.stubAthenaGetQueryExecution(retryCount, finalQueryExecutionStatus)
    awsMockServer.stubAthenaGetQueryResults(queryResults)
  }

  protected fun stubFailedQueryExecution(
    queryExecutionId: String,
  ) {
    awsMockServer.stubAthenaStartQueryExecution(queryExecutionId)
    awsMockServer.stubAthenaGetQueryExecution(1, "FAILED")
  }

  protected fun verifyAthenaStartQueryExecutionCount(
    count: Int,
  ) {
    awsMockServer.verify(
      count,
      postRequestedFor(urlPathEqualTo("/"))
        .withHeader("X-Amz-Target", equalTo("AmazonAthena.StartQueryExecution")),
    )
  }

  protected fun getAllServeEvents(): List<ServeEvent> = awsMockServer.allServeEvents

  protected fun verifyAthenaGetQueryExecutionCount(
    count: Int,
  ) {
    awsMockServer.verify(
      count,
      postRequestedFor(urlPathEqualTo("/"))
        .withHeader("X-Amz-Target", equalTo("AmazonAthena.GetQueryExecution")),
    )
  }

  protected fun verifyAthenaGetQueryResultsCount(
    count: Int,
  ) {
    awsMockServer.verify(
      count,
      postRequestedFor(urlPathEqualTo("/"))
        .withHeader("X-Amz-Target", equalTo("AmazonAthena.GetQueryResults")),
    )
  }
}
