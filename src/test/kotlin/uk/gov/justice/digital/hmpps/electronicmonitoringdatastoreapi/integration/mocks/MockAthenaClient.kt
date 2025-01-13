package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.integration.mocks

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentListQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaStringQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSubjectHistoryReportQuery

@Component
@Profile("integration")
class MockAthenaClient : AthenaClientInterface {
  override fun getQueryResult(athenaQuery: AthenaQuery, role: AthenaRole?): ResultSet = when (athenaQuery) {
    is AthenaStringQuery -> handleQuery(athenaQuery)
    is AthenaOrderSearchQuery -> AthenaHelper.Companion.resultSetFromJson(orderSearchResultSet())
    is AthenaKeyOrderInformationQuery -> AthenaHelper.Companion.resultSetFromJson(keyOrderInformationResultSet())
    is AthenaSubjectHistoryReportQuery -> AthenaHelper.Companion.resultSetFromJson(subjectHistoryReportResultSet())
    is AthenaDocumentListQuery -> AthenaHelper.Companion.resultSetFromJson(documentListResultSet())

    else -> throw RuntimeException("Unexpected query type ${athenaQuery.javaClass}")
  }

  fun handleQuery(athenaQuery: AthenaStringQuery): ResultSet {
    if (athenaQuery.queryString == "THROW ERROR") {
      throw IllegalArgumentException("I threw an error")
    }

    return AthenaHelper.Companion.resultSetFromJson(orderSearchResultSet())
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
    { "VarCharValue": "$value" }
  """.trimIndent()

  fun orderSearchResultData(subjectId: String, fullName: String) = """
    {
      "Data": [
        ${varCharValueColumn(subjectId)},
        ${varCharValueColumn(fullName)},
        ${varCharValueColumn("address line 1")},
        ${varCharValueColumn("address line 2")},
        ${varCharValueColumn("")},
        ${varCharValueColumn("postcode")},
        ${varCharValueColumn("01/01/1970")},
        ${varCharValueColumn("01/01/1970")}
      ]
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
        ${varCharValueColumn("https://example.com/report")},
        ${varCharValueColumn(fullName)},
        ${varCharValueColumn("01/01/2010")},
        ${varCharValueColumn("01:01:01.000")}
      ]
    }
  """.trimIndent()

  private fun documentData(
    name: String,
    url: String,
    createdOn: String,
    time: String,
    notes: String,
  ): String = """
    {
      "Data": [
        ${varCharValueColumn(name)},
        ${varCharValueColumn(url)},
        ${varCharValueColumn(createdOn)},
        ${varCharValueColumn(time)},
        ${varCharValueColumn(notes)},
      ]
    }
  """.trimIndent()

  fun orderSearchResultSet() = """
    {
      "ResultSet": {
        "Rows": [
          {
            "Data": [
              ${varCharValueColumn("legacy_subject_id")},
              ${varCharValueColumn("full_name")},
              ${varCharValueColumn("primary_address_line_1")},
              ${varCharValueColumn("primary_address_line_2")},
              ${varCharValueColumn("primary_address_line_3")},
              ${varCharValueColumn("primary_address_post_code")},
              ${varCharValueColumn("order_start_date")},
              ${varCharValueColumn("order_end_date")}
            ]
          },
          ${orderSearchResultData("1000000", "Amy Smith")},
          ${orderSearchResultData("2000000", "Bill Smith")},
          ${orderSearchResultData("3000000", "Claire Smith")},
          ${orderSearchResultData("8000000", "Daniel Smith")},
          ${orderSearchResultData("30000", "Emma Smith")},
          ${orderSearchResultData("4000000", "Fred Smith")},
        ],
        "ResultSetMetadata": {
          "ColumnInfo": [
            ${metaDataRow("legacy_subject_id")},
            ${metaDataRow("full_name")},
            ${metaDataRow("primary_address_line_1")},
            ${metaDataRow("primary_address_line_2")},
            ${metaDataRow("primary_address_line_3")},
            ${metaDataRow("primary_address_post_code")},
            ${metaDataRow("order_start_date")},
            ${metaDataRow("order_end_date")}
          ]
        }
      },
      "UpdateCount": 0
    }
  """.trimIndent()

  fun keyOrderInformationResultSet() = """
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
          ${keyOrderInformationData("1253587", "1253587", "ELLEN RIPLEY")},
          ${keyOrderInformationData("1034415", "1032792", "JOHN BROWNLIE")}
        ],
        "ResultSetMetadata": {
          "ColumnInfo": [
            ${metaDataRow("legacy_subject_id")},
            ${metaDataRow("legacy_order_id")},
            ${metaDataRow("full_name")},
            ${metaDataRow("date_of_birth")},
            ${metaDataRow("primary_address_line_1")},
            ${metaDataRow("primary_address_line_2")},
            ${metaDataRow("primary_address_line_3")},
            ${metaDataRow("primary_address_post_code")},
            ${metaDataRow("order_start_date")},
            ${metaDataRow("order_end_date")}
          ]
        }
      },
      "UpdateCount": 0
    }
  """.trimIndent()

  fun subjectHistoryReportResultSet() = """
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
          ${subjectHistoryReportData("ELLEN RIPLEY")},
          ${subjectHistoryReportData("JOHN BROWNLIE")}
        ],
        "ResultSetMetadata": {
          "ColumnInfo": [
            ${metaDataRow("report_url")},
            ${metaDataRow("name")},
            ${metaDataRow("created_on")},
            ${metaDataRow("time")}
          ]
        }
      },
      "UpdateCount": 0
    }
  """.trimIndent()

  fun documentListResultSet() = """
    {
      "ResultSet": {
        "Rows": [
          {
            "Data": [
              ${varCharValueColumn("name")},
              ${varCharValueColumn("url")},
              ${varCharValueColumn("created_on")},
              ${varCharValueColumn("time")},
              ${varCharValueColumn("notes")}
            ]
          },
          ${documentData(name = "Document 1", url = "https://example.com/document-1", createdOn = "01-02-2020", time = "0100", notes = "Order 1 documents xxxxxx xxxxxxx NAME HDC New.msg application/octet-stream NAME - TOLD TO IGNORE AS SUBJECT STILL IN CUSTODY ABCD 12 345678 xx-xxxxxx")},
          ${documentData(name = "Document 2", url = "https://example.com/document-2", createdOn = "21-09-2017", time = "0200", notes = "Order 2 documents xxxxxx xxxxxxx NAME HDC New.msg application/octet-stream NAME - TOLD TO IGNORE AS SUBJECT STILL IN CUSTODY ABCD 12 345678 xx-xxxxxx")},
          ${documentData(name = "Document 3", url = "https://example.com/document-3", createdOn = "08-04-2021", time = "0300", notes = "Order 3 documents xxxxxx xxxxxxx NAME HDC New.msg application/octet-stream NAME - TOLD TO IGNORE AS SUBJECT STILL IN CUSTODY ABCD 12 345678 xx-xxxxxx")},
          ${documentData(name = "Document 4", url = "https://example.com/document-4", createdOn = "09-12-2015", time = "0400", notes = "Order 4 documents xxxxxx xxxxxxx NAME HDC New.msg application/octet-stream NAME - TOLD TO IGNORE AS SUBJECT STILL IN CUSTODY ABCD 12 345678 xx-xxxxxx")},
          ${documentData(name = "Document 5", url = "https://example.com/document-5", createdOn = "04-09-2011", time = "1300", notes = "Order 5 documents xxxxxx xxxxxxx NAME HDC New.msg application/octet-stream NAME - TOLD TO IGNORE AS SUBJECT STILL IN CUSTODY ABCD 12 345678 xx-xxxxxx")},
          ${documentData(name = "Document 6", url = "https://example.com/document-6", createdOn = "09-12-2001", time = "0500", notes = "Order 6 documents xxxxxx xxxxxxx NAME HDC New.msg application/octet-stream NAME - TOLD TO IGNORE AS SUBJECT STILL IN CUSTODY ABCD 12 345678 xx-xxxxxx")},
          ${documentData(name = "Document 7", url = "https://example.com/document-7", createdOn = "09-12-2008", time = "0600", notes = "Order 7 documents xxxxxx xxxxxxx NAME HDC New.msg application/octet-stream NAME - TOLD TO IGNORE AS SUBJECT STILL IN CUSTODY ABCD 12 345678 xx-xxxxxx")},
          ${documentData(name = "Document 8", url = "https://example.com/document-8", createdOn = "09-12-2011", time = "0700", notes = "Order 8 documents xxxxxx xxxxxxx NAME HDC New.msg application/octet-stream NAME - TOLD TO IGNORE AS SUBJECT STILL IN CUSTODY ABCD 12 345678 xx-xxxxxx")},
          ${documentData(name = "Document 9", url = "https://example.com/document-9", createdOn = "09-12-2012", time = "0800", notes = "Order 9 documents xxxxxx xxxxxxx NAME HDC New.msg application/octet-stream NAME - TOLD TO IGNORE AS SUBJECT STILL IN CUSTODY ABCD 12 345678 xx-xxxxxx")},
          ${documentData(name = "Document 10", url = "https://example.com/document-10", createdOn = "09-12-2002", time = "0900", notes = "Order 10 documents xxxxxx xxxxxxx NAME HDC New.msg application/octet-stream NAME - TOLD TO IGNORE AS SUBJECT STILL IN CUSTODY ABCD 12 345678 xx-xxxxxx")},
          ${documentData(name = "Document 11", url = "https://example.com/document-11", createdOn = "09-12-2007", time = "1400", notes = "Order 11 documents xxxxxx xxxxxxx NAME HDC New.msg application/octet-stream NAME - TOLD TO IGNORE AS SUBJECT STILL IN CUSTODY ABCD 12 345678 xx-xxxxxx")},
          ${documentData(name = "Document 12", url = "https://example.com/document-12", createdOn = "09-12-2006", time = "1000", notes = "Order 12 documents xxxxxx xxxxxxx NAME HDC New.msg application/octet-stream NAME - TOLD TO IGNORE AS SUBJECT STILL IN CUSTODY ABCD 12 345678 xx-xxxxxx")},
          ${documentData(name = "Document 13", url = "https://example.com/document-13", createdOn = "09-12-2005", time = "1100", notes = "Order 13 documents xxxxxx xxxxxxx NAME HDC New.msg application/octet-stream NAME - TOLD TO IGNORE AS SUBJECT STILL IN CUSTODY ABCD 12 345678 xx-xxxxxx")},
          ${documentData(name = "Document 14", url = "https://example.com/document-14", createdOn = "09-12-2003", time = "1200", notes = "Order 14 documents xxxxxx xxxxxxx NAME HDC New.msg application/octet-stream NAME - TOLD TO IGNORE AS SUBJECT STILL IN CUSTODY ABCD 12 345678 xx-xxxxxx")}
        ],
        "ResultSetMetadata": {
          "ColumnInfo": [
            ${metaDataRow("name")},
            ${metaDataRow("url")},
            ${metaDataRow("created_on")},
            ${metaDataRow("time")},
            ${metaDataRow("notes")}
          ]
        }
      },
      "UpdateCount": 0
    }
  """.trimIndent()
}
