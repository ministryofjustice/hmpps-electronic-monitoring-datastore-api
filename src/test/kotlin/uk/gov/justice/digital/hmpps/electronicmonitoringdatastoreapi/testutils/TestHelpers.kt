package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils

class TestHelpers {
  companion object {
    fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")
  }
}
