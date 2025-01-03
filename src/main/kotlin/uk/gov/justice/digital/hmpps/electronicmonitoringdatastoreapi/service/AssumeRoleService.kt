package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sts.StsClient
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest
import software.amazon.awssdk.services.sts.model.Credentials

enum class AthenaRole(val iamRole: String) {
  DEV("arn:aws:iam::800964199911:role/cmt_read_emds_data_dev"),
  TEST("arn:aws:iam::396913731313:role/cmt_read_emds_data_test"),
  ;

  companion object {
    inline fun fromString(name: String): AthenaRole? = enumValues<AthenaRole>().find { it.name == name }
  }
}

class AssumeRoleService {
  fun getModernisationPlatformRole(): Credentials {
    val modernisationPlatformRole: String = "arn:aws:iam::800964199911:role/cmt_read_emds_data_dev" // TODO: Move this to config!
    val sessionId: String = "DataStoreApiSession" // args[1]
    val region: Region = Region.EU_WEST_2

    val stsClient: StsClient = StsClient.builder()
      .credentialsProvider(DefaultCredentialsProvider.builder().build())
      .region(region)
      .build()

    val assumeRoleRequest = AssumeRoleRequest.builder()
      .roleArn(modernisationPlatformRole)
      .roleSessionName(sessionId)
      .build()

    val assumeRoleResponse = stsClient.assumeRole(assumeRoleRequest)

    return assumeRoleResponse.credentials()
  }

  fun getModernisationPlatformCredentialsProvider(role: AthenaRole): StsAssumeRoleCredentialsProvider {
    val sessionId: String = "DataStoreApiSession" // args[1]
    val region: Region = Region.EU_WEST_2

    val stsClient: StsClient = StsClient.builder()
      .credentialsProvider(DefaultCredentialsProvider.builder().build())
      .region(region)
      .build()

    return StsAssumeRoleCredentialsProvider.builder()
      .stsClient(stsClient)
      .refreshRequest { builder ->
        builder.roleArn(role.iamRole).roleSessionName(sessionId)
      }
      .build()
  }
}
