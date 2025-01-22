package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sts.StsClient
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider

class EmDatastoreRoleProvider {
  companion object {
    const val SESSION_ID: String = "DataStoreApiSession"
    val region: Region = Region.EU_WEST_2

    fun getRole(role: AthenaRole): AwsCredentialsProvider {
      val useLocalCredentials: Boolean = System.getenv("FLAG_USE_LOCAL_CREDS").toBoolean()

      return if (useLocalCredentials) {
        EnvironmentVariableCredentialsProvider.create()
      } else {
        getModernisationPlatformCredentialsProvider(role)
      }
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
