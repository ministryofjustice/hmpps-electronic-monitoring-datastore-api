package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.queryBuilders

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.TestHelpers
import java.time.LocalDate
import java.time.LocalDateTime

const val INPUT_TO_SHOW_HIDDEN_RECORDS = "12345 OR 1=1--"
const val INPUT_TO_JOIN_OTHER_TABLES = "' UNION SELECT username, password FROM users--"

class SqlQueryBuilderTest {

  data class TestEntityModel(
    val textField: String,
    val booleanField: Boolean,
    val intField: Int,
    val longField: Long,
    val floatField: Float,
    val doubleField: Double,
    val dateField: LocalDate,
    val datetimeField: LocalDateTime,
    val charField: Char,
  )

  class TestableSqlQueryBuilder : SqlQueryBuilderBase<TestEntityModel>(TestEntityModel::class)

  @Nested
  inner class FindAll {

    val findAllQuery: String = """
        SELECT
           boolean_field,
           char_field,
           date_field,
           datetime_field,
           double_field,
           float_field,
           int_field,
           long_field,
           text_field
        FROM 
          fake_database.test_entity_model
    """.trimIndent()

    @Test
    fun `returns valid SQL`() {
      val expectedSQL = TestHelpers.replaceWhitespace(findAllQuery.trimIndent())

      val result = TestableSqlQueryBuilder().findAll().build("fake_database")

      Assertions.assertThat(TestHelpers.replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
      Assertions.assertThat(result.parameters).hasSize(0)
    }
  }

  @Nested
  inner class FindByLegacySubjectId {

    val findByLegacySubjectIdQuery: String = """
        SELECT
           boolean_field,
           char_field,
           date_field,
           datetime_field,
           double_field,
           float_field,
           int_field,
           long_field,
           text_field
        FROM 
          fake_database.test_entity_model
        WHERE 
           UPPER(CAST(legacy_subject_id as varchar)) = ?
    """.trimIndent()

    @Test
    fun `returns valid SQL`() {
      val legacySubjectId = "AA122333"

      val expectedSQL = TestHelpers.replaceWhitespace(findByLegacySubjectIdQuery.trimIndent())

      val result = TestableSqlQueryBuilder().findByLegacySubjectId(legacySubjectId).build("fake_database")

      Assertions.assertThat(TestHelpers.replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
      Assertions.assertThat(result.parameters).hasSize(1)
      Assertions.assertThat(result.parameters).isEqualTo(arrayOf("UPPER('$legacySubjectId')"))
    }

    @Test
    fun `throws an error if input contains SQL injection attack to show hidden records`() {
      Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
        TestableSqlQueryBuilder().findByLegacySubjectId(INPUT_TO_SHOW_HIDDEN_RECORDS).build("fake_database")
      }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
    }

    @Test
    fun `throws an error if input contains SQL injection attack to join other tables`() {
      Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
        TestableSqlQueryBuilder().findByLegacySubjectId(INPUT_TO_JOIN_OTHER_TABLES).build("fake_database")
      }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
    }
  }

  @Nested
  inner class GetByLegacySubjectId {

    val getByLegacySubjectIdQuery: String = """
        SELECT
           boolean_field,
           char_field,
           date_field,
           datetime_field,
           double_field,
           float_field,
           int_field,
           long_field,
           text_field
        FROM 
          fake_database.test_entity_model
        WHERE 
           UPPER(CAST(legacy_subject_id as varchar)) = ?
    """.trimIndent()

    @Test
    fun `returns valid SQL`() {
      val legacySubjectId = "AA122333"

      val expectedSQL = TestHelpers.replaceWhitespace(getByLegacySubjectIdQuery.trimIndent())

      val result = TestableSqlQueryBuilder().getByLegacySubjectId(legacySubjectId).build("fake_database")

      Assertions.assertThat(TestHelpers.replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
      Assertions.assertThat(result.parameters).hasSize(1)
      Assertions.assertThat(result.parameters).isEqualTo(arrayOf("UPPER('$legacySubjectId')"))
    }

    @Test
    fun `throws an error if input contains SQL injection attack to show hidden records`() {
      Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
        TestableSqlQueryBuilder().getByLegacySubjectId(INPUT_TO_SHOW_HIDDEN_RECORDS).build("fake_database")
      }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
    }

    @Test
    fun `throws an error if input contains SQL injection attack to join other tables`() {
      Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
        TestableSqlQueryBuilder().getByLegacySubjectId(INPUT_TO_JOIN_OTHER_TABLES).build("fake_database")
      }.withMessage("legacy_subject_id must only contain alphanumeric characters and spaces")
    }
  }

  @Nested
  inner class FindByCriteria {

    val findByCriteriaQuery: String = """
        SELECT
           boolean_field,
           char_field,
           date_field,
           datetime_field,
           double_field,
           float_field,
           int_field,
           long_field,
           text_field
        FROM 
          fake_database.test_entity_model
        WHERE 
           UPPER(CAST(legacy_subject_id as varchar)) = ?
    """.trimIndent()

    @Test
    fun `returns valid SQL`() {
      val criteria = OrderSearchCriteria(legacySubjectId = "AA122333")

      val expectedSQL = TestHelpers.replaceWhitespace(findByCriteriaQuery.trimIndent())

      val result = TestableSqlQueryBuilder().findBy(criteria).build("fake_database")

      Assertions.assertThat(TestHelpers.replaceWhitespace(result.queryString)).isEqualTo(expectedSQL)
      Assertions.assertThat(result.parameters).hasSize(1)
      Assertions.assertThat(result.parameters).isEqualTo(arrayOf("UPPER('%${criteria.legacySubjectId}%')"))
    }

    @Test
    fun `throws an error if input contains SQL injection attack to show hidden records`() {
      val criteria = OrderSearchCriteria(firstName = INPUT_TO_SHOW_HIDDEN_RECORDS)

      Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
        TestableSqlQueryBuilder().findBy(criteria).build("fake_database")
      }.withMessage("first_name must only contain alphanumeric characters and spaces")
    }

    @Test
    fun `throws an error if input contains SQL injection attack to join other tables`() {
      val criteria = OrderSearchCriteria(lastName = INPUT_TO_JOIN_OTHER_TABLES)

      Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
        TestableSqlQueryBuilder().findBy(criteria).build("fake_database")
      }.withMessage("last_name must only contain alphanumeric characters and spaces")
    }
  }
}
