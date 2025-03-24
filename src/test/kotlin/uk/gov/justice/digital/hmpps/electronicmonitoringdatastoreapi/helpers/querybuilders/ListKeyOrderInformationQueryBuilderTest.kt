package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class ListKeyOrderInformationQueryBuilderTest {

  fun replaceWhitespace(text: String): String = text.replace("\\s+".toRegex(), " ")

  val baseQuery: String = """
      SELECT
        legacy_subject_id
      FROM 
        fake_database.order_details
  """.trimIndent()

  @Test
  fun `returns valid SQL if legacySubjectId is populated`() {
    val expectedSQL = replaceWhitespace(baseQuery.trimIndent())

    val result = ListKeyOrderInformationQueryBuilder("fake_database")
      .build()

    Assertions.assertThat(replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
    Assertions.assertThat(result.parameters).isEqualTo(arrayOf<String>())
  }
}
