package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils

class MockAthenaResultSetBuilder(
  val columns: Map<String, String>,
  val rows: Array<Array<String>>,
) {
  constructor(
    columns: Array<String>,
    rows: Array<Array<String>>,
  ) : this(columns.associateWith { "varchar" }, rows)

  private val validTypes = listOf("varchar", "boolean", "bigInt")

  private fun metaDataRow(label: String, type: String): String {
    if (!validTypes.contains(type)) {
      throw Exception("Type $type not a valid column type")
    }

    val precision = if (type == "bigInt") {
      17
    } else {
      2147483647
    }

    return """
    {
      "CatalogName": "hive",
      "SchemaName": "",
      "TableName": "",
      "Name": "$label",
      "Label": "$label",
      "Type": "$type",
      "Precision": $precision,
      "Scale": 0,
      "Nullable": "UNKNOWN",
      "CaseSensitive": true
    }
    """.trimIndent()
  }

  private fun valueColumn(value: String) = if (value.isEmpty()) {
    "{}"
  } else {
    """{ "VarCharValue": "$value" }"""
  }.trimIndent()

  private fun row(data: Array<String>) = """
    {
      "Data": [
        ${data.joinToString(",\n") { value -> valueColumn(value) }}
      ]
    }
  """.trimIndent()

  fun build(): String = """
    {
      "ResultSet": {
        "Rows": [${arrayOf(row(this.columns.keys.toTypedArray()), this.rows.joinToString(",\n") { data -> row(data) }).joinToString(",\n")}
        ],
        "ResultSetMetadata": {
          "ColumnInfo": [
            ${this.columns.entries.joinToString(",\n") { column -> metaDataRow(column.key, column.value) }}
          ]
        }
      },
      "UpdateCount": 0
    }
  """.trimIndent()
}
