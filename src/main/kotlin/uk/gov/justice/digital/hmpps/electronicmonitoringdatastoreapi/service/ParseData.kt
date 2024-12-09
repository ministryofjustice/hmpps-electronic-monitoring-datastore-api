package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.json.JSONObject

class ParseData {

  val sampleData: JSONObject = JSONObject(
    """{
  "ResultSet": {
    "Rows": [
      {
        "Data": [
          {
            "VarCharValue": "legacy_subject_id"
          },
          {
            "VarCharValue": "legacy_order_id"
          },
          {
            "VarCharValue": "first_name"
          },
          {
            "VarCharValue": "last_name"
          },
          {
            "VarCharValue": "full_name"
          }
        ]
      },
      {
        "Data": [
          {
            "VarCharValue": "1253587"
          },
          {
            "VarCharValue": "1250042"
          },
          {
            "VarCharValue": "ELLEN"
          },
          {
            "VarCharValue": "RIPLY"
          },
          {
            "VarCharValue": "ELLEN RIPLY"
          }
        ]
      },
      {
        "Data": [
          {
            "VarCharValue": "1034415"
          },
          {
            "VarCharValue": "1032792"
          },
          {
            "VarCharValue": "JOHN"
          },
          {
            "VarCharValue": "BROWNLIE"
          },
          {
            "VarCharValue": "JOHN BROWNLIE"
          }
        ]
      }
    ],
    "ResultSetMetadata": {
      "ColumnInfo": [
        {
          "CatalogName": "hive",
          "SchemaName": "",
          "TableName": "",
          "Name": "legacy_subject_id",
          "Label": "legacy_subject_id",
          "Type": "bigint",
          "Precision": 19,
          "Scale": 0,
          "Nullable": "UNKNOWN",
          "CaseSensitive": false
        },
        {
          "CatalogName": "hive",
          "SchemaName": "",
          "TableName": "",
          "Name": "legacy_order_id",
          "Label": "legacy_order_id",
          "Type": "bigint",
          "Precision": 19,
          "Scale": 0,
          "Nullable": "UNKNOWN",
          "CaseSensitive": false
        },
        {
          "CatalogName": "hive",
          "SchemaName": "",
          "TableName": "",
          "Name": "first_name",
          "Label": "first_name",
          "Type": "varchar",
          "Precision": 2147483647,
          "Scale": 0,
          "Nullable": "UNKNOWN",
          "CaseSensitive": true
        },
        {
          "CatalogName": "hive",
          "SchemaName": "",
          "TableName": "",
          "Name": "last_name",
          "Label": "last_name",
          "Type": "varchar",
          "Precision": 2147483647,
          "Scale": 0,
          "Nullable": "UNKNOWN",
          "CaseSensitive": true
        },
        {
          "CatalogName": "hive",
          "SchemaName": "",
          "TableName": "",
          "Name": "full_name",
          "Label": "full_name",
          "Type": "varchar",
          "Precision": 2147483647,
          "Scale": 0,
          "Nullable": "UNKNOWN",
          "CaseSensitive": true
        }
      ]
    }
  },
  "UpdateCount": 0
}""",
  )

  fun parse(): Boolean {
//    val temp = GetQueryResultsResponse

    return true
  }
}
