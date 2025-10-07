package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.queryBuilders

import io.zeko.db.sql.Query
import io.zeko.db.sql.QueryBlock
import io.zeko.db.sql.dsl.eq
import io.zeko.db.sql.dsl.like
import jakarta.validation.Valid
import org.apache.commons.lang3.StringUtils
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AlphanumericSnakeCaseStrategy
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaMapper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass

interface SqlQueryBuilder {
  fun build(databaseName: String): AthenaQuery
  fun findAll(): SqlQueryBuilder
  fun findByLegacySubjectId(legacySubjectId: String?): SqlQueryBuilder
  fun getByLegacySubjectId(legacySubjectId: String?): SqlQueryBuilder
  fun findBy(@Valid criteria: OrderSearchCriteria, fuzzy: Boolean = true): SqlQueryBuilder
}

open class SqlQueryBuilderBase<T : Any>(
  open val tableName: String,
  open val recordKClass: KClass<T>,
) : SqlQueryBuilder {
  companion object {
    fun translate(input: String?): String? = AlphanumericSnakeCaseStrategy().translate(input)
  }

  constructor(kClass: KClass<T>) : this(translate(kClass.simpleName)!!, kClass)

  var whereClauses: MutableMap<String, QueryBlock> = mutableMapOf()
    protected set

  var values: MutableList<String> = mutableListOf()
    protected set

  override fun findAll(): SqlQueryBuilder = this

  override fun findByLegacySubjectId(legacySubjectId: String?): SqlQueryBuilder = findBy(OrderSearchCriteria(legacySubjectId = legacySubjectId), false)

  override fun getByLegacySubjectId(legacySubjectId: String?): SqlQueryBuilder = findBy(OrderSearchCriteria(legacySubjectId = legacySubjectId), false)

  override fun findBy(@Valid criteria: OrderSearchCriteria, fuzzy: Boolean): SqlQueryBuilder {
    val criteria = AthenaMapper(OrderSearchCriteria::class).toCriteria(criteria)

    criteria.forEach { (key, value) ->
      if (value != null && value != "") {
        if (value is String) {
          validateAlphanumericSpace(value, key)
        }

        val type = when (value) {
          is Boolean -> "boolean"
          is Int -> "bigint"
          is LocalDate -> "date"
          is LocalDateTime -> "date"
          else -> "varchar"
        }

        if (fuzzy) {
          addFuzzyParameter(key, value, type)
        } else {
          addStrictParameter(key, value, type)
        }
      }
    }

    return this
  }

  private fun validateAlphanumericSpace(value: String?, field: String) {
    if (value.isNullOrBlank()) {
      return
    }

    if (!StringUtils.isAlphanumericSpace(value)) {
      throw IllegalArgumentException("$field must only contain alphanumeric characters and spaces")
    }
  }

  private fun validateAlphanumeric(value: String?, field: String) {
    if (value.isNullOrBlank()) {
      return
    }

    if (!StringUtils.isAlphanumeric(value)) {
      throw IllegalArgumentException("$field must only contain alphanumeric characters and spaces")
    }
  }

  @Suppress("UNCHECKED_CAST")
  private fun getSQL(databaseName: String): String {
    val columns = AthenaMapper(recordKClass).getColumns()
    val query = Query().fields(*columns).from(tableName)

    whereClauses.forEach {
      query.where(it.value)
    }

    return query.toSql()
  }

  private fun addFuzzyParameter(key: String, value: Any, type: String) {
    val normalisedValue = "UPPER('%$value%')"
    values.add(normalisedValue)
    whereClauses[key] = "UPPER(CAST($key as $type))" like normalisedValue
  }

  private fun addStrictParameter(key: String, value: Any, type: String) {
    val normalisedValue = when (type) {
      "varchar" -> "UPPER('$value')"
      "date" -> OffsetDateTime.of(value as LocalDateTime, ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME)
      else -> value.toString()
    }
    values.add(normalisedValue)
    whereClauses[key] = "UPPER(CAST($key as $type))" eq normalisedValue
  }

  override fun build(databaseName: String): AthenaQuery = AthenaQuery(getSQL(databaseName), values.toTypedArray())
}
