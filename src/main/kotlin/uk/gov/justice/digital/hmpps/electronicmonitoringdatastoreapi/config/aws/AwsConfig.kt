package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.aws

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import software.amazon.awssdk.services.athena.AthenaClient
import software.amazon.awssdk.services.sts.StsClient
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider
import java.net.URI

@Configuration
@EnableConfigurationProperties(AwsProperties::class)
class AwsConfig(
  private val properties: AwsProperties,
) {
  companion object {
    const val SESSION_ID: String = "ContractManagementApiSession"
  }

  @Bean
  fun stsClient(): StsClient {
    val clientBuilder = StsClient.builder()
      .region(properties.region)

    if (properties.endpointUrl != null) {
      clientBuilder.endpointOverride(URI(properties.endpointUrl))
    }

    return clientBuilder.build()
  }

  @Bean
  @Primary
  @Qualifier("athenaGeneralClient")
  fun athenaGeneralClient(): AthenaClient {
    val clientBuilder = AthenaClient.builder()
      .region(properties.region)

    if (properties.endpointUrl != null) {
      clientBuilder.endpointOverride(URI(properties.endpointUrl))
    }

    if (properties.athena.roleGeneral != null) {
      clientBuilder.credentialsProvider(
        StsAssumeRoleCredentialsProvider.builder().stsClient(stsClient())
          .refreshRequest { builder ->
            builder.roleArn(properties.athena.roleGeneral).roleSessionName(SESSION_ID)
          }
          .build(),
      )
    }

    return clientBuilder.build()
  }

  @Bean
  @Qualifier("athenaRestrictedClient")
  fun athenaRestrictedClient(): AthenaClient {
    val clientBuilder = AthenaClient.builder()
      .region(properties.region)

    if (properties.endpointUrl != null) {
      clientBuilder.endpointOverride(URI(properties.endpointUrl))
    }

    if (properties.athena.roleRestricted != null) {
      clientBuilder.credentialsProvider(
        StsAssumeRoleCredentialsProvider.builder().stsClient(stsClient())
          .refreshRequest { builder ->
            builder.roleArn(properties.athena.roleRestricted).roleSessionName(SESSION_ID)
          }
          .build(),
      )
    }

    return clientBuilder.build()
  }
}
