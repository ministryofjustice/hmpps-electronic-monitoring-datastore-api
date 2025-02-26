package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.JwtAuthorisationHelper

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

  @Autowired
  protected lateinit var jwtAuthHelper: JwtAuthorisationHelper

  internal fun setAuthorisation(
    username: String? = "AUTH_ADM",
    roles: List<String> = listOf("ROLE_EM_DATASTORE_GENERAL_RO"),
    scopes: List<String> = listOf("read"),
  ): (
    HttpHeaders,
  ) -> Unit = jwtAuthHelper.setAuthorisationHeader(
    username = username,
    scope = scopes,
    roles = roles,
  )

  internal fun setAuthorisationWithoutUsername(
    roles: List<String> = listOf("ROLE_EM_DATASTORE_GENERAL_RO"),
    scopes: List<String> = listOf("read"),
  ): (HttpHeaders) -> Unit = setAuthorisation(username = "", scopes = scopes, roles= roles)

  protected fun stubPingWithResponse(status: Int) {
    hmppsAuth.stubHealthPing(status)
  }
}
