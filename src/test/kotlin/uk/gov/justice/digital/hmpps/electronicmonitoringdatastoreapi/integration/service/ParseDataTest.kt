package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.service

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.ParseData

class ParseDataTest {

  @Test
  fun `Loads the data as an object`() {
    val sut = ParseData()

    val result = sut.parse()
  }
}
