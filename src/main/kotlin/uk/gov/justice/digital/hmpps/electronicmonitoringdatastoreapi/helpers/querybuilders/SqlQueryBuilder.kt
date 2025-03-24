package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

import io.zeko.db.sql.Query
import io.zeko.db.sql.dsl.eq

open class SqlQueryBuilder(
  open val databaseName: String,
  val table: String,
  val fields: Array<String>,
) {
  protected val parameters: MutableMap<String, String> = mutableMapOf<String, String>()

  protected fun getSQL(): String {
    var query = Query()
      .fields(*fields)
      .from("$databaseName.$table")

    parameters.forEach {
      query.where(it.key.toString() eq it.value.toString())
    }

    return query.toSql()
  }
}
