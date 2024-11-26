package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.common.DateTimeUnit
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.time.LocalDate

class AwsMockServerExtension :
BeforeAllCallback,
AfterAllCallback,
BeforeEachCallback {
  companion object {
    @JvmField
    val awsApi = AwsMockServer()
  }

  override fun beforeAll(context: ExtensionContext): Unit = awsApi.start()
  override fun beforeEach(context: ExtensionContext): Unit = awsApi.resetAll()
  override fun afterAll(context: ExtensionContext): Unit = awsApi.stop()
}

class AwsMockServer : WireMockServer(WIREMOCK_PORT) {
  companion object {
    private const val WIREMOCK_PORT = 8095 // TODO: Check, but I think this should be the local app port

    private const val SESSION_TOKEN = "fake-session-token-response"
    private const val ACCESS_KEY_ID = "fake-access-key-id-response"
    private const val SECRET_ACCESS_KEY = "fake-secret-access-key-response"
  }
  fun stubStsAssumeRoleApi() {
    stubFor(
      post("/")
        .withHeader("X-Amz-Target", containing("GetCallerIdentity"))
        .willReturn(
          aResponse()
            .withHeader("Content-Type", "x-amz-json-1.1") // TODO: Check response header
            .withBody(
              """
              {
                "Credentials":: {
                  "AccessKeyId": "$ACCESS_KEY_ID",
                  "SecretAccessKey": "$SECRET_ACCESS_KEY",
                  "SessionToken": "$SESSION_TOKEN",
                  "Expiration": "${LocalDate.now().plus(30, DateTimeUnit.MINUTES.toTemporalUnit())}"
                }
              }""".trimIndent(), // TODO: Check response body
        )
      ),
    )
  }

  fun stubHealthPing(status: Int) {
    stubFor(
      get("/health/ping").willReturn(
        aResponse()
          .withHeader("Content-Type", "application/json")
          .withBody("""{"status":"${if (status == 200) "UP" else "DOWN"}"}""")
          .withStatus(status),
      ),
    )
  }

}