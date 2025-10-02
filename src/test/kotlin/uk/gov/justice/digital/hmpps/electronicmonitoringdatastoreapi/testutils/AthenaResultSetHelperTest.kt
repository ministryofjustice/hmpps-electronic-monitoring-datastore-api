package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaMapper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.OrderDetails

data class MiniOrder(
  val firstName: String,
  val fullName: String,
  val lastName: String,
  val legacyOrderId: Long,
  val legacySubjectId: Long,
)

class AthenaResultSetHelperTest {
  val defaultResultSet = MockAthenaResultSetBuilder(
    columns = mapOf(
      "legacy_subject_id" to "bigint",
      "legacy_order_id" to "bigint",
      "first_name" to "varchar",
      "last_name" to "varchar",
      "full_name" to "varchar",
    ),
    rows = arrayOf(
      arrayOf("1253587", "1250042", "ELLEN", "RIPLY", "ELLEN RIPLY"),
      arrayOf("1034415", "1032792", "JOHN", "BROWNLIE", "JOHN BROWNLIE"),
    ),
  ).toString()

  @Test
  fun `Can convert JSON to ResultSet object`() {
    val simpleResultTest: String = """
      {
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
      }
    """.trimIndent()

    val resultSet: ResultSet = AthenaResultSetHelper.resultSetFromJson(simpleResultTest)

    Assertions.assertThat(resultSet).isInstanceOf(ResultSet::class.java)
  }

  @Test
  fun `Can extract column names from ColumnInfo object`() {
    val resultSet: ResultSet = AthenaResultSetHelper.resultSetFromJson(defaultResultSet)

    val expectedColumns: Map<String, Int> = mapOf(
      Pair("legacy_subject_id", 0),
      Pair("legacy_order_id", 1),
      Pair("first_name", 2),
      Pair("last_name", 3),
      Pair("full_name", 4),
    )

    val actualColumns = AthenaResultSetHelper.mapColumns(resultSet)

    Assertions.assertThat(actualColumns).isEqualTo(expectedColumns)
  }

  @Test
  fun `Can extract a list of properties from a class`() {
    data class ArbitraryClass(
      val firstField: String,
      @Suppress("PropertyName") val BADLYNamedField: Boolean?,
      @Suppress("PropertyName") val ThirdField: Long = 4455566,
    )

    val expected = listOf("first_field", "badlynamed_field", "third_field")

    val result: List<String> = AthenaResultSetHelper.extractFieldNames(ArbitraryClass::class)

    Assertions.assertThat(result).hasSameElementsAs(expected)
  }

  @Test
  fun `Can check whether a set of fields exists in the mapped column output`() {
    val resultSet: ResultSet = AthenaResultSetHelper.resultSetFromJson(defaultResultSet)

    val testClass = MiniOrder::class
    val realColumns: List<String> = AthenaResultSetHelper.extractFieldNames(testClass)

    val fakeColumns: List<String> = listOf("cheeseburger", "last_name")

    Assertions.assertThat(AthenaResultSetHelper.checkRequiredColumns(resultSet, testClass)).isTrue()
    Assertions.assertThat(AthenaResultSetHelper.checkRequiredColumns(resultSet, realColumns)).isTrue()
    Assertions.assertThat(AthenaResultSetHelper.checkRequiredColumns(resultSet, fakeColumns)).isFalse()
  }

  @Nested
  inner class MapTo {

    @Test
    fun `Can map to AthenaOrderDTO`() {
      val resultSet: ResultSet = AthenaResultSetHelper.resultSetFromJson(
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
            "VarCharValue": "primary_address_line_1"
          },
          {
            "VarCharValue": "primary_address_line_2"
          },
          {
            "VarCharValue": "primary_address_line_3"
          },
          {
            "VarCharValue": "primary_address_post_code"
          },
          {
            "VarCharValue": "order_start_date"
          },
          {
            "VarCharValue": "order_end_date"
          }
        ]
      },
      {
        "Data": [
          {
            "VarCharValue": "1253587"
          },
          {
            "VarCharValue": "1253587"
          },
          {
            "VarCharValue": "ELLEN"
          },
          {
            "VarCharValue": "RIPLY"
          },
          {
            "VarCharValue": "310 Lightbowne Road, Moston"
          },
          {
            "VarCharValue": "Moston"
          },
          {
            "VarCharValue": "Manchester"
          },
          {
            "VarCharValue": "M40 0FJ"
          },
          {
            "VarCharValue": "2019-10-24"
          },
          {
            "VarCharValue": "2020-03-24"
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
          "Precision": "19",
          "Scale": "0",
          "Nullable": "UNKNOWN",
          "CaseSensitive": "false"
        },
        {
          "CatalogName": "hive",
          "SchemaName": "",
          "TableName": "",
          "Name": "legacy_order_id",
          "Label": "legacy_order_id",
          "Type": "bigint",
          "Precision": "19",
          "Scale": "0",
          "Nullable": "UNKNOWN",
          "CaseSensitive": "false"
        },
        {
          "CatalogName": "hive",
          "SchemaName": "",
          "TableName": "",
          "Name": "first_name",
          "Label": "first_name",
          "Type": "varchar",
          "Precision": "2147483647",
          "Scale": "0",
          "Nullable": "UNKNOWN",
          "CaseSensitive": "true"
        },
        {
          "CatalogName": "hive",
          "SchemaName": "",
          "TableName": "",
          "Name": "last_name",
          "Label": "last_name",
          "Type": "varchar",
          "Precision": "2147483647",
          "Scale": "0",
          "Nullable": "UNKNOWN",
          "CaseSensitive": "true"
        },
        {
          "CatalogName": "hive",
          "SchemaName": "",
          "TableName": "",
          "Name": "primary_address_line_1",
          "Label": "primary_address_line_1",
          "Type": "varchar",
          "Precision": "2147483647",
          "Scale": "0",
          "Nullable": "UNKNOWN",
          "CaseSensitive": "true"
        },
        {
          "CatalogName": "hive",
          "SchemaName": "",
          "TableName": "",
          "Name": "primary_address_line_2",
          "Label": "primary_address_line_2",
          "Type": "varchar",
          "Precision": "2147483647",
          "Scale": "0",
          "Nullable": "UNKNOWN",
          "CaseSensitive": "true"
        },
        {
          "CatalogName": "hive",
          "SchemaName": "",
          "TableName": "",
          "Name": "primary_address_line_3",
          "Label": "primary_address_line_3",
          "Type": "varchar",
          "Precision": "2147483647",
          "Scale": "0",
          "Nullable": "UNKNOWN",
          "CaseSensitive": "true"
        },
        {
          "CatalogName": "hive",
          "SchemaName": "",
          "TableName": "",
          "Name": "primary_address_post_code",
          "Label": "primary_address_post_code",
          "Type": "varchar",
          "Precision": "2147483647",
          "Scale": "0",
          "Nullable": "UNKNOWN",
          "CaseSensitive": "true"
        },
        {
          "CatalogName": "hive",
          "SchemaName": "",
          "TableName": "",
          "Name": "order_start_date",
          "Label": "order_start_date",
          "Type": "date",
          "Precision": "0",
          "Scale": "0",
          "Nullable": "UNKNOWN",
          "CaseSensitive": "false"
        },
        {
          "CatalogName": "hive",
          "SchemaName": "",
          "TableName": "",
          "Name": "order_end_date",
          "Label": "order_end_date",
          "Type": "date",
          "Precision": "0",
          "Scale": "0",
          "Nullable": "UNKNOWN",
          "CaseSensitive": "false"
        }
      ]
    }
  }
}""",
      )

      val expected: List<OrderDetails> = listOf(
        OrderDetails(
          legacySubjectId = "1253587",
          legacyOrderId = "1253587",
          firstName = "ELLEN",
          lastName = "RIPLY",
          primaryAddressLine1 = "310 Lightbowne Road, Moston",
          primaryAddressLine2 = "Moston",
          primaryAddressLine3 = "Manchester",
          primaryAddressPostCode = "M40 0FJ",
          orderStartDate = "2019-10-24",
          orderEndDate = "2020-03-24",
          offenceRisk = false,
        ),
      )

      val result = AthenaMapper(OrderDetails::class).mapTo(resultSet)

      Assertions.assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Can map the ResultSet to a generic object`() {
      val resultSet: ResultSet = AthenaResultSetHelper.resultSetFromJson(defaultResultSet)

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

      val result = AthenaMapper(MiniOrder::class).mapTo(resultSet)

      Assertions.assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Can handle null columns in a result set`() {
      val emptyValueResultTest: String = """
        {
          "ResultSet": {
            "Rows": [
              {
                "Data": [
                  {
                    "VarCharValue": "legacy_subject_id"
                  },
                  {
                    "VarCharValue": "null_field"
                  }
                ]
              },
              {
                "Data": [
                  {
                    "VarCharValue": "1253587"
                  },
                  {}
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
                  "Name": "null_field",
                  "Label": "null_field",
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
        }
      """.trimIndent()

      val resultSet = AthenaResultSetHelper.resultSetFromJson(emptyValueResultTest)

      Assertions.assertThat(resultSet.rows().size).isEqualTo(2)
      Assertions.assertThat(resultSet.rows()[1].data().size).isEqualTo(2)
      Assertions.assertThat(resultSet.rows()[1].data()[0].varCharValue()).isEqualTo("1253587")
      Assertions.assertThat(resultSet.rows()[1].data()[1].varCharValue()).isNull()
    }

    @Test
    fun `Mapping fails if mapping to object with wrong set of fields`() {
      val resultSet: ResultSet = AthenaResultSetHelper.resultSetFromJson(defaultResultSet)

      data class FakeObject(
        val firstField: String,
        val secondField: Boolean?,
        val thirdField: Long = 4455566,
      )

      Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java)
        .isThrownBy { AthenaMapper(FakeObject::class).mapTo(resultSet) }
    }
  }
}
