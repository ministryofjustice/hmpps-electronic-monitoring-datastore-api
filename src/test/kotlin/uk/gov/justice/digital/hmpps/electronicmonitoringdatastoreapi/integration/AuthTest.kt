package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_GENERAL__RO

class AuthTest : IntegrationTestBase() {
  @Test
  fun `Providing no JWT to a secured endpoint returns 401`() {
    webTestClient.get()
      .uri("/orders/integrity/123")
      .exchange()
      .expectStatus()
      .isUnauthorized
      .expectBody()
  }

  @Test
  fun `Providing malformed JWT to a secured endpoint returns 401`() {
    webTestClient.get()
      .uri("/orders/integrity/123")
      .header("Authorization", "Bearer sdhidsofhoi")
      .exchange()
      .expectStatus()
      .isUnauthorized
  }

  @Test
  fun `Providing JWT signed by wrong private key to a secured endpoint returns 401`() {
    @Suppress("ktlint:standard:max-line-length")
    val jwt = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhcHByb3ZlZC1wcmVtaXNlcy1hcGkiLCJncmFudF90eXBlIjoiY2xpZW50X2NyZWRlbnRpYWxzIiwic2NvcGUiOlsicmVhZCJdLCJhdXRoX3NvdXJjZSI6Im5vbmUiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjkwOTEvYXV0aC9pc3N1ZXIiLCJleHAiOjI2NTk3MDQ5NDAsImF1dGhvcml0aWVzIjpbIlJPTEVfSU5URVJWRU5USU9OUyIsIlJPTEVfT0FTWVNfUkVBRF9PTkxZIiwiUk9MRV9DT01NVU5JVFkiLCJST0xFX0dMT0JBTF9TRUFSQ0giLCJST0xFX0NPTU1VTklUWV9VU0VSUyIsIlJPTEVfUklTS19TVU1NQVJZIl0sImp0aSI6ImlTSEtsTXJ1aXFUNjF0dTNXVFFqckE2WWJfTSIsImNsaWVudF9pZCI6ImFwcHJvdmVkLXByZW1pc2VzLWFwaSJ9.Cr7Nl09vjUpyieddsJwyQF02nmqhR6PbM4xePA47ukkyhhctE4SwqpOAO5D5OIstr9ePnlmF_Tug7HZ6-SLF8lBnN9C_M2-74d8127gPkQxjWsGnAKIxAGDnwLjtwV1UpSvS0p-Phg3cBTGiq6_HABEuh2JSD67eJS0ZaqNPUXXp2kTfi1ZJXA1ysxFKvAP5qYHbBpYWfvFq9Wkpsrq4sM41yjzS7hmkpaEUAYvKUdYefeRAT6nMCU6pfkEOoCmXkMTf6n6rJ1HxxTvkucZwEQk1dOKZUH0d_AOjZy5RAXiSRzgiYsMfB02gvn2T0FfOyjkjKXgVDsFc2yf3bd6P0g"

    webTestClient.get()
      .uri("/orders/integrity/123")
      .header("Authorization", "Bearer $jwt")
      .exchange()
      .expectStatus()
      .isUnauthorized
  }

  @Test
  fun `Providing JWT without user name claim returns 401`() {
    webTestClient.get()
      .uri("/orders/integrity/123")
      .headers(setAuthorisationWithoutUsername())
      .exchange()
      .expectStatus()
      .isUnauthorized
  }

  @Test
  fun `Providing JWT without right role returns 403`() {
    webTestClient.get()
      .uri("/orders/integrity/123")
      .headers(setAuthorisation(roles = emptyList()))
      .exchange()
      .expectStatus()
      .isForbidden
  }

  @Test
  fun `Providing JWT with restricted request without restricted role returns 403`() {
    webTestClient.get()
      .uri("/orders/integrity/123?restricted=true")
      .headers(setAuthorisation(roles = listOf(ROLE_EM_DATASTORE_GENERAL__RO)))
      .exchange()
      .expectStatus()
      .isForbidden
  }
}
