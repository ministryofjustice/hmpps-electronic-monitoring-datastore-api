package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client


import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
class AthenaClientFactory {

  fun acquireCredentials(): Boolean {

    val credentialsProvider = DefaultCredentialsProvider.builder().build()

    return true;
  }

}