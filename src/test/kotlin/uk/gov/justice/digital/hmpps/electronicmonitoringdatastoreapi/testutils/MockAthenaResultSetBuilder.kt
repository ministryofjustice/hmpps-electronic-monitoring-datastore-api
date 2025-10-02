package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils

import software.amazon.awssdk.services.athena.model.ResultSet

class MockAthenaResultSetBuilder(
  val columns: Map<String, String>,
  val rows: Array<Array<String>>,
) {
  constructor(
    columns: Array<String>,
    rows: Array<Array<String>>,
  ) : this(columns.associateWith { "varchar" }, rows)

  private val validTypes = listOf("varchar", "boolean", "bigint", "date")

  private fun metaDataRow(label: String, type: String): String {
    if (!validTypes.contains(type)) {
      throw Exception("Type $type not a valid column type")
    }

    val precision = when (type) {
      "bigint" -> 19
      "varchar" -> 2147483647
      else -> 0
    }

    val caseSensitive = when (type) {
      "varchar" -> "true"
      else -> "false"
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
      "CaseSensitive": $caseSensitive
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

  fun toResultSet(): ResultSet = AthenaResultSetHelper.resultSetFromJson(toString())

  override fun toString(): String = """
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
