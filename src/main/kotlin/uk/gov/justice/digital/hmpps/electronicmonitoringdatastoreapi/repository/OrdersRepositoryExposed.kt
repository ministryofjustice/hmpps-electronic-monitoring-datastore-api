package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.QueryBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greater
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.vendors.H2Dialect

class OrdersRepositoryExposed {
  object Temp : Table("order_details") {
    val legacySubjectId = integer("legacy_subject_id")
  }

  fun generate(): String {
//    val dialect: H2Dialect = H2Dialect()

    val query: Query = Temp.select(listOf(Temp.legacySubjectId))

    val builder: QueryBuilder = QueryBuilder(false)

    val result: String = query.prepareSQL(builder)

    return result
  }
}
