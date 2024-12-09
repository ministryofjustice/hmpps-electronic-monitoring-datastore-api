package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.json.JSONObject
import software.amazon.awssdk.services.athena.model.ColumnInfo
import software.amazon.awssdk.services.athena.model.ColumnNullable
import software.amazon.awssdk.services.athena.model.Datum
import software.amazon.awssdk.services.athena.model.ResultSet
import software.amazon.awssdk.services.athena.model.ResultSetMetadata
import software.amazon.awssdk.services.athena.model.Row
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.MiniOrder

class ParseData {
  fun resultSetFromJson(string: String): ResultSet = resultSetFromJson(JSONObject(string))

  fun resultSetFromJson(jsonData: JSONObject): ResultSet {
    val rows: List<Row> = jsonData
      .getJSONObject("ResultSet")
      .getJSONArray("Rows")
      .map { row ->
        Row.builder()
          .data(
            (row as JSONObject).getJSONArray("Data").map { dataJson ->
              Datum.builder()
                .varCharValue((dataJson as JSONObject).optString("VarCharValue", null))
                .build()
            },
          )
          .build()
      }

    val columnInfo: List<ColumnInfo> = jsonData
      .getJSONObject("ResultSet")
      .getJSONObject("ResultSetMetadata")
      .getJSONArray("ColumnInfo")
      .map { columnJson ->
        val column = columnJson as JSONObject
        ColumnInfo.builder()
          .catalogName(column.getString("CatalogName"))
          .schemaName(column.getString("SchemaName"))
          .tableName(column.getString("TableName"))
          .name(column.getString("Name"))
          .label(column.getString("Label"))
          .type(column.getString("Type"))
          .precision(column.getInt("Precision"))
          .scale(column.getInt("Scale"))
          .nullable(ColumnNullable.valueOf(column.getString("Nullable").uppercase()))
          .caseSensitive(column.getBoolean("CaseSensitive"))
          .build()
      }

    val resultSet: ResultSet = ResultSet.builder()
      .rows(rows)
      .resultSetMetadata(
        ResultSetMetadata.builder()
          .columnInfo(columnInfo)
          .build(),
      )
      .build()

    return resultSet
  }

  fun parseOrders(resultSet: ResultSet): List<MiniOrder> = listOf(
    MiniOrder(
      legacySubjectId = 1234567,
      legacyOrderId = 1250042,
      firstName = "ELLEN",
      lastName = "RIPLY",
      fullName = "ELLEN RIPLY",
    ),
    MiniOrder(
      legacySubjectId = 1034415,
      legacyOrderId = 1032792,
      firstName = "JOHN",
      lastName = "BROWNLIE",
      fullName = "JOHN BROWNLIE",
    ),
  )

  fun mapColumns(resultSet: ResultSet): Map<String, Int> = resultSet.resultSetMetadata().columnInfo()
    .mapIndexed { index, columnInfo -> columnInfo.name() to index }
    .toMap()

  fun checkRequiredColumns(resultSet: ResultSet, requiredColumns: List<String>): Boolean {
    val allPresent: Boolean = requiredColumns.all { it in mapColumns(resultSet) }
    return allPresent
  }

  fun maptoOrders(resultSet: ResultSet): List<MiniOrder> {
    val mapper = jacksonObjectMapper()
      .registerKotlinModule()
      .apply {
        propertyNamingStrategy = com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE
      }

    val recievedColumnNames: List<String> = resultSet.resultSetMetadata().columnInfo().map { it.name() }

    val mappedRows: List<Map<String, String?>> = resultSet.rows().drop(1).map { row ->
      row.data().mapIndexed { index, datum ->
        recievedColumnNames[index] to datum.varCharValue()
      }.toMap()
    }

    return mappedRows.map { row ->
      mapper.convertValue(row, MiniOrder::class.java)
    }
  }
}
