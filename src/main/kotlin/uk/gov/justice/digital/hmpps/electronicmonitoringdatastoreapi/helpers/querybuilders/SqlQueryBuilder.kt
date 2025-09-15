package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import io.zeko.db.sql.Query
import io.zeko.db.sql.QueryBlock
import org.apache.commons.lang3.StringUtils.isAlphanumeric
import org.apache.commons.lang3.StringUtils.isAlphanumericSpace

abstract class SqlQueryBuilder(
  open val tableName: String,
  private val fields: Array<String>,
) {
  var whereClauses: MutableMap<String, QueryBlock> = mutableMapOf()
    protected set
  var values: MutableList<String> = mutableListOf()
    protected set

  protected fun validateAlphanumericSpace(value: String?, field: String) {
    if (value.isNullOrBlank()) {
      return
    }

    if (!isAlphanumericSpace(value)) {
      throw IllegalArgumentException("$field must only contain alphanumeric characters and spaces")
    }
  }

  @Suppress("SameParameterValue")
  protected fun validateAlphanumeric(value: String?, field: String) {
    if (value.isNullOrBlank()) {
      return
    }

    if (!isAlphanumeric(value)) {
      throw IllegalArgumentException("$field must only contain alphanumeric characters and spaces")
    }
  }

  protected fun getSQL(databaseName: String): String {
    val query = Query()
      .fields(*fields)
      .from("$databaseName.$tableName")

    whereClauses.forEach {
      query.where(it.value)
    }

    return query.toSql()
  }

  abstract fun build(databaseName: String): AthenaQuery
}
