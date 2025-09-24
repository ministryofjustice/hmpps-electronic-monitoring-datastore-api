package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.aws

import org.springframework.boot.context.properties.ConfigurationProperties
import software.amazon.awssdk.regions.Region

@ConfigurationProperties(prefix = "aws")
data class AwsProperties(
  val athena: AthenaProperties = AthenaProperties(),

  val endpointUrl: String? = null,

  val region: Region,
)
