package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.json.JSONArray
import org.json.JSONObject
import software.amazon.awssdk.services.athena.model.*
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.MiniOrder

class ParseData {
  fun resultSetFromJson(string: String): ResultSet {
    return resultSetFromJson(JSONObject(string))
  }

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
          })
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
          .build()
      )
      .build()

    return resultSet
  }

  fun parseOrders(resultSet: ResultSet): List<MiniOrder> {
    return listOf(
      MiniOrder(
        legacySubjectId = 1234567,
        legacyOrderId = 1250042,
        firstName = "ELLEN",
        lastName = "RIPLY",
        fullName = "ELLEN RIPLY"
      ),
      MiniOrder(
        legacySubjectId = 1034415,
        legacyOrderId = 1032792,
        firstName = "JOHN",
        lastName = "BROWNLIE",
        fullName = "JOHN BROWNLIE"
      ),
    )
  }
}
