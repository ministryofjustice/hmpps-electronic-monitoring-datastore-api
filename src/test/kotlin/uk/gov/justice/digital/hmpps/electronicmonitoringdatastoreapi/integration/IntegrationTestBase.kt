package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import software.amazon.awssdk.services.sqs.model.PurgeQueueRequest
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_GENERAL__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.config.PostgresContainer
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.config.registerPostgresProperties
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.testcontainers.LocalStackContainer
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.testcontainers.LocalStackContainer.setLocalStackProperties
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.wiremock.AwsApiExtension
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.wiremock.AwsApiExtension.Companion.awsMockServer
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.wiremock.HmppsAuthApiExtension
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.wiremock.HmppsAuthApiExtension.Companion.hmppsAuth
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingQueueException
import uk.gov.justice.hmpps.test.kotlin.auth.JwtAuthorisationHelper

@ExtendWith(HmppsAuthApiExtension::class, AwsApiExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
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

  @Autowired
  protected lateinit var hmppsQueueService: HmppsQueueService

  private val auditQueue by lazy { hmppsQueueService.findByQueueId("audit") ?: throw MissingQueueException("HmppsQueue audit not found") }

  protected val auditSqsClient by lazy { auditQueue.sqsClient }

  protected val auditQueueUrl by lazy { auditQueue.queueUrl }

  internal fun setAuthorisation(
    username: String? = "AUTH_ADM",
    roles: List<String> = listOf(ROLE_EM_DATASTORE_GENERAL__RO),
    scopes: List<String> = listOf("read"),
  ): (HttpHeaders) -> Unit = jwtAuthHelper.setAuthorisationHeader(username = username, scope = scopes, roles = roles)

  protected fun stubPingWithResponse(status: Int) {
    hmppsAuth.stubHealthPing(status)
  }

  @BeforeEach
  fun `clear queues`() {
    auditSqsClient.purgeQueue(PurgeQueueRequest.builder().queueUrl(auditQueueUrl).build()).get()
  }

  companion object {

    private val postgres = PostgresContainer.instance
    private val localStackContainer = LocalStackContainer.instance

    @Suppress("unused")
    @JvmStatic
    @DynamicPropertySource
    fun testcontainers(registry: DynamicPropertyRegistry) {
      localStackContainer?.also { setLocalStackProperties(it, registry) }
      registry.registerPostgresProperties(postgres)
    }
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
}
