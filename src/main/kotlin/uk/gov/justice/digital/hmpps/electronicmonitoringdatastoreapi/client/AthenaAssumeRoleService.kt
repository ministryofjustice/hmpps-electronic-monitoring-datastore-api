package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sts.StsClient
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest
import software.amazon.awssdk.services.sts.model.Credentials

class AthenaAssumeRoleService {
  companion object {
    const val SESSION_ID: String = "DataStoreApiSession"
    val region: Region = Region.EU_WEST_2

    fun getModernisationPlatformRole(role: AthenaRole): Credentials {
      val stsClient = StsClient.builder()
        .credentialsProvider(DefaultCredentialsProvider.builder().build())
        .region(region)
        .build()

      val assumeRoleRequest = AssumeRoleRequest.builder()
        .roleArn(role.iamRole)
        .roleSessionName(SESSION_ID)
        .build()

      val assumeRoleResponse = stsClient.assumeRole(assumeRoleRequest)
      val credentials = assumeRoleResponse.credentials()

      return credentials
    }

    fun getModernisationPlatformCredentialsProvider(role: AthenaRole): StsAssumeRoleCredentialsProvider {
      val stsClient = StsClient.builder()
        .credentialsProvider(DefaultCredentialsProvider.builder().build())
        .region(region)
        .build()

      val credentialsProvider = StsAssumeRoleCredentialsProvider.builder()
        .stsClient(stsClient)
        .refreshRequest { builder ->
          builder.roleArn(role.iamRole).roleSessionName(SESSION_ID)
        }
        .build()

      return credentialsProvider
    }
  }
}
