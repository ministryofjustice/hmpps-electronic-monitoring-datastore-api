package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration

import io.jsonwebtoken.Jwts
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.wiremock.HmppsAuthApiExtension
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.wiremock.HmppsAuthApiExtension.Companion.hmppsAuth
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.mocks.MockEmDatastoreClient
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.time.Duration
import java.util.Date
import java.util.UUID

@ExtendWith(HmppsAuthApiExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
abstract class IntegrationTestBase {

  @AfterEach
  fun teardown() {
    MockEmDatastoreClient.reset()
  }

  @Autowired
  protected lateinit var webTestClient: WebTestClient

  internal fun setAuthorisation(
    username: String? = "AUTH_ADM",
    roles: List<String> = listOf("ROLE_EM_DATASTORE_GENERAL_RO"),
    scopes: List<String> = listOf("read"),
  ): (
    HttpHeaders,
  ) -> Unit = setAuthorisationHeader(
    username = username,
    scope = scopes,
    roles = roles,
  )

  internal fun setAuthorisationWithoutUsername(
    roles: List<String> = listOf("ROLE_EM_DATASTORE_GENERAL_RO"),
    scopes: List<String> = listOf("read"),
  ): (HttpHeaders) -> Unit = setAuthorisation(username = "")

  protected fun stubPingWithResponse(status: Int) {
    hmppsAuth.stubHealthPing(status)
  }

  internal fun setAuthorisationHeader(
    clientId: String = "test-client-id",
    username: String? = null,
    scope: List<String> = listOf(),
    roles: List<String> = listOf(),
  ): (HttpHeaders) -> Unit {
    val token = createJwtAccessTokenWithMFA(
      clientId = clientId,
      username = username,
      scope = scope,
      roles = roles,
    )
    return { it.setBearerAuth(token) }
  }

  internal fun createJwtAccessTokenWithMFA(
    clientId: String = "test-client-id",
    username: String? = null,
    scope: List<String>? = listOf(),
    roles: List<String>? = listOf(),
    expiryTime: Duration = Duration.ofHours(2),
    jwtId: String = UUID.randomUUID().toString(),
    authSource: String = "none",
    grantType: String = "client_credentials",
    passedMfa: Boolean = true,
  ): String = mutableMapOf<String, Any>(
    "sub" to (username ?: clientId),
    "client_id" to clientId,
    "auth_source" to authSource,
    "grant_type" to grantType,
    "passed_mfa" to passedMfa,
  ).apply {
    username?.let { this["user_name"] = username }
    scope?.let { this["scope"] = scope }
    roles?.let {
      // ensure that all roles have a ROLE_ prefix
      this["authorities"] = roles.map { "ROLE_${it.substringAfter("ROLE_")}" }
    }
  }
    .let {
      Jwts.builder()
        .id(jwtId)
        .subject(username ?: clientId)
        .claims(it.toMap())
        .expiration(Date(System.currentTimeMillis() + expiryTime.toMillis()))
        .signWith(keyPair.private, Jwts.SIG.RS256)
        .compact()
    }

  private val keyPair: KeyPair = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair()
}
