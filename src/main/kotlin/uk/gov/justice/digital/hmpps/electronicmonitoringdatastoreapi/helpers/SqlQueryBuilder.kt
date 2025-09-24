package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers

import io.zeko.db.sql.Query
import io.zeko.db.sql.QueryBlock
import org.apache.commons.lang3.StringUtils
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import kotlin.reflect.KClass

interface SqlQueryBuilder {
  fun build(databaseName: String): AthenaQuery
}

abstract class SqlQueryBuilderBase<T : Any>(
  open val tableName: String,
  open val kClass: KClass<T>,
) : SqlQueryBuilder {
  var whereClauses: MutableMap<String, QueryBlock> = mutableMapOf()
    protected set
  var values: MutableList<String> = mutableListOf()
    protected set

  protected fun validateAlphanumericSpace(value: String?, field: String) {
    if (value.isNullOrBlank()) {
      return
    }

    if (!StringUtils.isAlphanumericSpace(value)) {
      throw IllegalArgumentException("$field must only contain alphanumeric characters and spaces")
    }
  }

  protected fun validateAlphanumeric(value: String?, field: String) {
    if (value.isNullOrBlank()) {
      return
    }

    if (!StringUtils.isAlphanumeric(value)) {
      throw IllegalArgumentException("$field must only contain alphanumeric characters and spaces")
    }
  }

  @Suppress("UNCHECKED_CAST")
  private fun getSQL(databaseName: String): String {
    val columns = AthenaHelper.toColumns(kClass as KClass<Any>)
    val query = Query().fields(*columns).from("$databaseName.$tableName")

    whereClauses.forEach {
      query.where(it.value)
    }

    return query.toSql()
  }

  override fun build(databaseName: String): AthenaQuery = AthenaQuery(getSQL(databaseName), values.toTypedArray())
}
