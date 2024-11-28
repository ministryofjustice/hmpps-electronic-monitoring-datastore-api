package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider

class AthenaClientFactoryTest {

  @Nested
  inner class `Load credentials from runtime environment` {

    @Test
    fun `should exist`() {
      val myFactory = AthenaClientFactory()

      Assertions.assertNotNull(myFactory)
    }

    @Test
    fun `has a creds method`() {
      val myFactory = AthenaClientFactory()
      val expected: String = "DefaultCredentialsProvider(providerChain=LazyAwsCredentialsProvider(delegate=Lazy(value=Uninitialized)))"
      val result: DefaultCredentialsProvider = myFactory.acquireCredentials()

      Assertions.assertEquals(expected, result.toString())
    }
  }

//  @Nested
//  inner class `build athena client` {
//
//    @Test
//    fun `builds a client`() {
//      val myFactory = AthenaClientFactory();
//      val result = myFactory.athenaClient()
//
//      Assertions.assertNotNull(result)
//    }
//  }
}
