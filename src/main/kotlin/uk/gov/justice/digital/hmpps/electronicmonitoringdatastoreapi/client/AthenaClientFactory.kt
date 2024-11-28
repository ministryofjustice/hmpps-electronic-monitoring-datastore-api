package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.athena.AthenaClient

class AthenaClientFactory {

  fun acquireCredentials(): DefaultCredentialsProvider {
    val credentialsProvider = DefaultCredentialsProvider.builder().build()

    return credentialsProvider
  }

  private val builder = AthenaClient.builder()
    .region(Region.EU_WEST_2)
//    .credentialsProvider(ProfileCredentialsProvider.create())
    .credentialsProvider(DefaultCredentialsProvider.builder().build())

  fun athenaClient(credentials: DefaultCredentialsProvider): AthenaClient = AthenaClient.builder()
    .region(Region.EU_WEST_2)
    .credentialsProvider(credentials)
    .build()
}
