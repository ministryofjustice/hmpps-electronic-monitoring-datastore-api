package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils

class MockAthenaResultSetBuilder(
  val columns: Array<String>,
  val rows: Array<Array<String>>,
) {
  private val validTypes = listOf("varchar", "boolean")

  private fun metaDataRow(label: String, type: String = "varchar"): String {
    if (!validTypes.contains(type)) {
      throw Exception("Type $type not a valid column type")
    }

    return """
    {
      "CatalogName": "hive",
      "SchemaName": "",
      "TableName": "",
      "Name": "$label",
      "Label": "$label",
      "Type": "$type",
      "Precision": 2147483647,
      "Scale": 0,
      "Nullable": "UNKNOWN",
      "CaseSensitive": true
    }
    """.trimIndent()
  }

  private fun varCharValueColumn(value: String) = if (value.isEmpty()) {
    "{}"
  } else {
    """{ "VarCharValue": "$value" }"""
  }.trimIndent()

  private fun row(data: Array<String>) = """
    {
      "Data": [
        ${data.joinToString(",\n") { value -> varCharValueColumn(value) }}
      ]
    }
  """.trimIndent()

  private fun resultSet(
    columns: Array<String>,
    rows: Array<Array<String>>,
  ) = """
    {
      "ResultSet": {
        "Rows": [
          ${arrayOf(row(columns), rows.joinToString(",\n") { data -> row(data) }).joinToString(",\n")}
        ],
        "ResultSetMetadata": {
          "ColumnInfo": [
            ${columns.joinToString(",\n") { column -> metaDataRow(column) }}
          ]
        }
      },
      "UpdateCount": 0
    }
  """.trimIndent()

  fun build(): String = resultSet(columns, rows)
}
