package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSubjectHistoryReportDTO
import java.util.UUID

class OrderInformationRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: OrderInformationRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = mock(EmDatastoreClient::class.java)
    repository = OrderInformationRepository(emDatastoreClient)
  }

  @Test
  fun `OrderInformationRepository can be instantiated`() {
    val sut = OrderInformationRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

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

  fun varCharValueColumn(value: String) = """
    {
      "VarCharValue": "$value"
    }
  """.trimIndent()

  fun keyOrderInformationData(subjectId: String, orderId: String, fullName: String) = """
    {
      "Data": [
        ${varCharValueColumn(subjectId)},
        ${varCharValueColumn(orderId)},
        ${varCharValueColumn(fullName)},
        ${varCharValueColumn("01/01/1970")},
        ${varCharValueColumn("address line 1")},
        ${varCharValueColumn("address line 2")},
        ${varCharValueColumn("")},
        ${varCharValueColumn("postcode")},
        ${varCharValueColumn("01/01/1970")},
        ${varCharValueColumn("01/01/1970")}
      ]
    }
  """.trimIndent()

  fun subjectHistoryReportData(fullName: String) = """
    {
      "Data": [
        ${varCharValueColumn("http://example.com/report")},
        ${varCharValueColumn(fullName)},
        ${varCharValueColumn("01/01/2010")},
        ${varCharValueColumn("01:01:01.000")}
      ]
    }
  """.trimIndent()

  fun keyOrderInformationResultSet(firstOrderId: String? = "1253587", firstFullName: String? = "ELLEN RIPLEY") = """
    {
      "ResultSet": {
        "Rows": [
          {
            "Data": [
              ${varCharValueColumn("legacy_subject_id")},
              ${varCharValueColumn("legacy_order_id")},
              ${varCharValueColumn("full_name")},
              ${varCharValueColumn("date_of_birth")},
              ${varCharValueColumn("primary_address_line_1")},
              ${varCharValueColumn("primary_address_line_2")},
              ${varCharValueColumn("primary_address_line_3")},
              ${varCharValueColumn("primary_address_post_code")},
              ${varCharValueColumn("order_start_date")},
              ${varCharValueColumn("order_end_date")}
            ]
          },
          ${keyOrderInformationData(firstOrderId!!, firstOrderId, firstFullName!!)},
          ${keyOrderInformationData("1034415", "1032792", "JOHN BROWNLIE")}
        ],
        "ResultSetMetadata": {
          "ColumnInfo": [
            """ + metaDataRow("legacy_subject_id") + """,
            """ + metaDataRow("legacy_order_id") + """,
            """ + metaDataRow("full_name") + """,
            """ + metaDataRow("date_of_birth") + """,
            """ + metaDataRow("primary_address_line_1") + """,
            """ + metaDataRow("primary_address_line_2") + """,
            """ + metaDataRow("primary_address_line_3") + """,
            """ + metaDataRow("primary_address_post_code") + """,
            """ + metaDataRow("order_start_date") + """,
            """ + metaDataRow("order_end_date") + """
          ]
        }
      },
      "UpdateCount": 0
    }
  """.trimIndent()

  fun subjectHistoryReportResultSet(firstFullName: String? = "ELLEN RIPLEY") = """
    {
      "ResultSet": {
        "Rows": [
          {
            "Data": [
              ${varCharValueColumn("report_url")},
              ${varCharValueColumn("name")},
              ${varCharValueColumn("created_on")},
              ${varCharValueColumn("time")}
            ]
          },
          ${subjectHistoryReportData(firstFullName!!)},
          ${subjectHistoryReportData("JOHN BROWNLIE")}
        ],
        "ResultSetMetadata": {
          "ColumnInfo": [
            """ + metaDataRow("report_url") + """,
            """ + metaDataRow("name") + """,
            """ + metaDataRow("created_on") + """,
            """ + metaDataRow("time") + """
          ]
        }
      },
      "UpdateCount": 0
    }
  """.trimIndent()

  @Nested
  inner class GetKeyOrderInformation {
    @Test
    fun `getKeyOrderInformation calls getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(keyOrderInformationResultSet())

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      repository.getKeyOrderInformation("123", AthenaRole.DEV)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getKeyOrderInformation returns an AthenaKeyOrderInformationDTO`() {
      val resultSet = AthenaHelper.resultSetFromJson(keyOrderInformationResultSet())

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getKeyOrderInformation("123", AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(AthenaKeyOrderInformationDTO::class.java)
    }

    @Test
    fun `getKeyOrderInformation returns the first result from getQueryResult`() {
      val orderId = UUID.randomUUID().toString()
      val fullName = "TEST NAME"

      val resultSet = AthenaHelper.resultSetFromJson(keyOrderInformationResultSet(orderId, fullName))

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getKeyOrderInformation(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.legacyOrderId).isEqualTo(orderId)
      Assertions.assertThat(result.legacySubjectId).isEqualTo(orderId)
      Assertions.assertThat(result.name).isEqualTo(fullName)
    }
  }

  @Nested
  inner class GetSubjectHistoryReport {
    @Test
    fun `getSubjectHistoryReport passes correct query to getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(subjectHistoryReportResultSet())

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      repository.getSubjectHistoryReport("123", AthenaRole.DEV)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getSubjectHistoryReport returns an AthenaSubjectHistoryReportDTO`() {
      val resultSet = AthenaHelper.resultSetFromJson(subjectHistoryReportResultSet())

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getSubjectHistoryReport("123", AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(AthenaSubjectHistoryReportDTO::class.java)
    }

    @Test
    fun `getSubjectHistoryReport returns the first result from getQueryResult`() {
      val orderId = UUID.randomUUID().toString()
      val fullName = "TEST NAME"

      val resultSet = AthenaHelper.resultSetFromJson(subjectHistoryReportResultSet(fullName))

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getSubjectHistoryReport(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.name).isEqualTo(fullName)
    }
  }
}
