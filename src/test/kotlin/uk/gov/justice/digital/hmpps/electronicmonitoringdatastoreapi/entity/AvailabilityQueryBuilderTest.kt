package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class AvailabilityQueryBuilderTest {

  fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")

  val baseQuery: String = """
      SELECT
        1
      FROM 
        fake_database.order_details
  """.trimIndent()

  @Test
  fun `returns valid SQL if legacySubjectId is populated`() {
    val expectedSQL = replaceWhitespace(baseQuery.trimIndent())

    val result = AthenaAvailabilityQueryBuilder()
      .build("fake_database")

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    Assertions.assertThat(result.parameters).isEqualTo(arrayOf<String>())
  }
}
