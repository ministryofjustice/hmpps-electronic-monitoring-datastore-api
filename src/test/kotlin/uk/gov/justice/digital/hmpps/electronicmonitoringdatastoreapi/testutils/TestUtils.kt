package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils

fun varCharValueColumn(value: String) = """
    {
      "VarCharValue": "$value"
    }
""".trimIndent()

fun metaDataRow(label: String, type: String = "varchar"): String = when (type) {
  "varchar" -> {
    """{
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
  }
  "boolean" -> {
    """{
    "CatalogName": "hive",
    "SchemaName": "",
    "TableName": "",
    "Name": "$label",
    "Label": "$label",
    "Type": "boolean",
    "Precision": 0,
    "Scale": 0,
    "Nullable": "UNKNOWN",
    "CaseSensitive": false
  }
    """.trimIndent()
  }
  else -> {
    throw Exception("No type specified")
  }
}
