package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class AthenaClientFactoryTest {

  @Nested
  inner class `Load credentials from runtime environment` {

    @Test
    fun `should exist`() {
      val myFactory = AthenaClientFactory();

      Assertions.assertNotNull(myFactory)
    }

    @Test
    fun `has a creds method`() {
      val myFactory = AthenaClientFactory();

      val result: Boolean = myFactory.acquireCredentials()

      Assertions.assertEquals(true, result)
    }
  }
}