package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.mocks

class MockAthenaResultSetBuilder(
  val columns: Array<String>,
  val rows: Array<Array<String>>,
) {
  companion object {
    fun metaDataRow(label: String) = """
      {
        "CatalogName": "hive",
        "SchemaName": "",
        "TableName": "",
        "Name": "$label",
        "Label": "$label",
        "Type": "varchar",
        "Precision": 2147483647,
        "Scale": 0,
        "Nullable": "UNKNOWN",
        "CaseSensitive": true
      }
    """.trimIndent()

    fun varCharValueColumn(value: String) = if (value.isEmpty()) {
      "{}"
    } else {
      """{ "VarCharValue": "$value" }"""
    }.trimIndent()

    fun row(data: Array<String>) = """
      {
        "Data": [
          ${data.joinToString(",\n") { value -> varCharValueColumn(value) }}
        ]
      }
    """.trimIndent()

    fun monitoringEventsResultSet(
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
  }

  fun build(): String = monitoringEventsResultSet(columns, rows)
}
