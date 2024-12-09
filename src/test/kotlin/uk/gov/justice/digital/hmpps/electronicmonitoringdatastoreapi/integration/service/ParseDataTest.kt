package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.service

import org.assertj.core.api.Assertions
import org.json.JSONObject
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.MiniOrder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.ParseData

class ParseDataTest {

  val defaultResultSet: String = """{
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
}
  """.trimIndent()

  @Test
  fun `Can convert JSON to ResultSet object`() {
    val sut = ParseData()

    val testResultSetJson: JSONObject = JSONObject(
      """{
  "ResultSet": {
    "Rows": [
      {
        "Data": [
          {
            "VarCharValue": "legacy_subject_id"
          }
        ]
      },
      {
        "Data": [
          {
            "VarCharValue": "1253587"
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
        }
      ]
    }
  },
  "UpdateCount": 0
}""",
    )

    val resultSet: ResultSet = sut.resultSetFromJson(testResultSetJson)

    Assertions.assertThat(resultSet).isInstanceOf(ResultSet::class.java)
  }

  @Test
  fun `Can map ResultSet to appropriate Order objects`() {
    val sut = ParseData()

    val resultSet: ResultSet = sut.resultSetFromJson(defaultResultSet)

    val expected: List<MiniOrder> = listOf(
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

    val result: List<MiniOrder> = sut.parseOrders(resultSet)

    Assertions.assertThat(result).isEqualTo(expected)
  }

  @Test
  fun `Can extract column names from ColumnInfo object`() {
    val sut = ParseData()

    val resultSet: ResultSet = sut.resultSetFromJson(defaultResultSet)

    val expectedColumns: Map<String, Int> = mapOf(
      Pair("legacy_subject_id", 0),
      Pair("legacy_order_id", 1),
      Pair("first_name", 2),
      Pair("last_name", 3),
      Pair("full_name", 4),
    )

    val actualColumns = sut.mapColumns(resultSet)

    Assertions.assertThat(actualColumns).isEqualTo(expectedColumns)
  }

  @Test
  fun `Can check a set of columns exists in the mapped column output`() {
    val sut = ParseData()

    val resultSet: ResultSet = sut.resultSetFromJson(defaultResultSet)

    val realColumns: List<String> = listOf(
      "legacy_subject_id",
      "legacy_order_id",
      "last_name",
      "full_name",
      "first_name",
    )

    val fakeColumns: List<String> = listOf("cheeseburger", "last_name")

    Assertions.assertThat(sut.checkRequiredColumns(resultSet, realColumns)).isTrue()
    Assertions.assertThat(sut.checkRequiredColumns(resultSet, fakeColumns)).isFalse()
  }

  @Test
  fun `Can map the ResultSet to a list of orders`() {
    val sut = ParseData()

    val resultSet: ResultSet = sut.resultSetFromJson(defaultResultSet)

//    data class fakeObject(
//      val firstField: String,
//      val secondField: Boolean?,
//      val thirdField: Long = 4455566
//    )
//
//    val expected = listOf<String>("firstField", "secondField", "thirdField")

    val expected: List<MiniOrder> = listOf(
      MiniOrder(
        legacySubjectId = 1253587,
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

    val result = sut.maptoOrders(resultSet)

    Assertions.assertThat(result).isEqualTo(expected)
  }
}
