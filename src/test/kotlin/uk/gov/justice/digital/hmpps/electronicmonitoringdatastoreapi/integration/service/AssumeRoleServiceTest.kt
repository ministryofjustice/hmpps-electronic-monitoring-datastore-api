//package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.service
//
//import com.github.tomakehurst.wiremock.client.WireMock
//import org.apache.http.impl.conn.Wire
//import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.Test
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
//import software.amazon.awssdk.services.sts.StsClient
//import software.amazon.awssdk.services.sts.model.AssumeRoleRequest
//import software.amazon.awssdk.services.sts.model.AssumeRoleResponse
//import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.wiremock.AwsMockServer
//import java.net.URI
//
//class AssumeRoleServiceTest {
//  @Test
//  fun `sts AssumeRole should return expected data from WireMock`() {
//    val accessKeyId = "fake-access-key-id"
//    val secretAccessKey = "fake-secret-access-key"
//    val modplatRoleArn = "arn:aws:iam::123456789876:role/testModPlatRole"
//    val modplatRoleSessionName = "testModPlatSession"
//    val wireMockServer = "http://localhost:8080"
//
//    val expectedAccessKey = "fake-access-key-id-response"
//    val expectedSecretAccessKey = "fake-secret-access-key-response"
//    val expectedSessionToken = "fake-session-token-response"
//
//    val mockAwsServer = AwsMockServer()
//
//    val stsClient = StsClient.builder()
//      .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
//      .endpointOverride(URI.create(wireMockServer))
//      .build()
//
//    val request = AssumeRoleRequest.builder()
//      .roleArn(modplatRoleArn)
//      .roleSessionName(modplatRoleSessionName)
//      .build()
//
//    val result: AssumeRoleResponse = stsClient.assumeRole(request)
//
//    Assertions.assertEquals(expectedAccessKey, result.credentials().accessKeyId())
//    Assertions.assertEquals(expectedSecretAccessKey, result.credentials().accessKeyId())
//    Assertions.assertEquals(expectedSessionToken, result.credentials().sessionToken())
//  }
//}