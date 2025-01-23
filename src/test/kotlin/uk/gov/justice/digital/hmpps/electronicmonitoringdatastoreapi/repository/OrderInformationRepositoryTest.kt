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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventListDTO
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

  @Nested
  inner class GetKeyOrderInformation {
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

    @Test
    fun `getKeyOrderInformation passes correct query to getQueryResult`() {
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

  @Nested
  inner class GetMonitoringEventsList {
    fun monitoringEventData(id: String) = """
      {
        "Data": [
          ${varCharValueColumn(id)},
          ${varCharValueColumn(id)},
          ${varCharValueColumn("TEST_EVENT")},
          ${varCharValueColumn("2001-01-01")},
          ${varCharValueColumn("01:01:01")},
          ${varCharValueColumn("1")},
          ${varCharValueColumn("2002-02-02")},
          ${varCharValueColumn("02:02:02")},
          ${varCharValueColumn("2")}
        ]
      }
    """.trimIndent()

    fun monitoringEventsResultSet(firstId: String = "987123") = """
      {
        "ResultSet": {
          "Rows": [
            {
              "Data": [
                ${varCharValueColumn("legacy_subject_id")},
                ${varCharValueColumn("legacy_order_id")},
                ${varCharValueColumn("event_type")},
                ${varCharValueColumn("event_date")},
                ${varCharValueColumn("event_time")},
                ${varCharValueColumn("event_second")},
                ${varCharValueColumn("process_date")},
                ${varCharValueColumn("process_time")},
                ${varCharValueColumn("process_second")}
              ]
            },
            ${monitoringEventData(firstId)},
            ${monitoringEventData("123456789")}
          ],
          "ResultSetMetadata": {
            "ColumnInfo": [
              ${metaDataRow("legacy_subject_id")},
              ${metaDataRow("legacy_order_id")},
              ${metaDataRow("event_type")},
              ${metaDataRow("event_date")},
              ${metaDataRow("event_time")},
              ${metaDataRow("event_second")},
              ${metaDataRow("process_date")},
              ${metaDataRow("process_time")},
              ${metaDataRow("process_second")}
            ]
          }
        },
        "UpdateCount": 0
      }
    """.trimIndent()

    @Test
    fun `getMonitoringEventsList passes correct query to getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(monitoringEventsResultSet())

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      repository.getMonitoringEventsList("123", AthenaRole.DEV)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getMonitoringEventsList returns an AthenaMonitoringEventListDTO`() {
      val resultSet = AthenaHelper.resultSetFromJson(monitoringEventsResultSet())

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getMonitoringEventsList("123", AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(AthenaMonitoringEventListDTO::class.java)
    }

    @Test
    fun `getMonitoringEventsList returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(monitoringEventsResultSet("987"))

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getMonitoringEventsList("987", AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.pageSize).isEqualTo(2)
      Assertions.assertThat(result.events.first().legacySubjectId).isEqualTo(987)
      Assertions.assertThat(result.events.first().legacyOrderId).isEqualTo(987)
      Assertions.assertThat(result.events.first().eventType).isEqualTo("TEST_EVENT")
    }
  }

  @Nested
  inner class GetIncidentEventsList {
    fun incidentEventData(id: String) = """
      {
        "Data": [
          ${varCharValueColumn(id)},
          ${varCharValueColumn(id)},
          ${varCharValueColumn("TEST_ALERT")},
          ${varCharValueColumn("2001-01-01")},
          ${varCharValueColumn("01:01:01")}
        ]
      }
    """.trimIndent()

    fun incidentEventsResultSet(firstId: String = "987123") = """
      {
        "ResultSet": {
          "Rows": [
            {
              "Data": [
                ${varCharValueColumn("legacy_subject_id")},
                ${varCharValueColumn("legacy_order_id")},
                ${varCharValueColumn("violation_alert_type")},
                ${varCharValueColumn("violation_alert_date")},
                ${varCharValueColumn("violation_alert_time")}
              ]
            },
            ${incidentEventData(firstId)},
            ${incidentEventData("123456789")}
          ],
          "ResultSetMetadata": {
            "ColumnInfo": [
              ${metaDataRow("legacy_subject_id")},
              ${metaDataRow("legacy_order_id")},
              ${metaDataRow("violation_alert_type")},
              ${metaDataRow("violation_alert_date")},
              ${metaDataRow("violation_alert_time")}
            ]
          }
        },
        "UpdateCount": 0
      }
    """.trimIndent()

    @Test
    fun `getIncidentEventsList passes correct query to getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(incidentEventsResultSet())

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      repository.getIncidentEventsList("123", AthenaRole.DEV)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getIncidentEventsList returns an AthenaIncidentEventListDTO`() {
      val resultSet = AthenaHelper.resultSetFromJson(incidentEventsResultSet())

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getIncidentEventsList("123", AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(AthenaIncidentEventListDTO::class.java)
    }

    @Test
    fun `getIncidentEventsList returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(incidentEventsResultSet("987"))

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getIncidentEventsList("987", AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.pageSize).isEqualTo(2)
      Assertions.assertThat(result.events.first().legacySubjectId).isEqualTo(987)
      Assertions.assertThat(result.events.first().legacyOrderId).isEqualTo(987)
      Assertions.assertThat(result.events.first().violationAlertType).isEqualTo("TEST_ALERT")
    }
  }

  @Nested
  inner class GetContactEventsList {
    fun contactEventData(id: String) = """
      {
        "Data": [
          ${varCharValueColumn(id)},
          ${varCharValueColumn(id)},
          ${varCharValueColumn("TEST_CONTACT")},
          ${varCharValueColumn("No reason")},
          ${varCharValueColumn("PHONE_CALL")},
          ${varCharValueColumn("usr_001")},
          ${varCharValueColumn("Bob the Builder")},
          ${varCharValueColumn("2001-01-01")},
          ${varCharValueColumn("01:01:01")},
          ${varCharValueColumn("2002-02-02")},
          ${varCharValueColumn("02:02:02")}
        ]
      }
    """.trimIndent()

    fun contactEventsResultSet(firstId: String = "987123") = """
      {
        "ResultSet": {
          "Rows": [
            {
              "Data": [
                ${varCharValueColumn("legacy_subject_id")},
                ${varCharValueColumn("legacy_order_id")},
                ${varCharValueColumn("contact_type")},
                ${varCharValueColumn("reason")},
                ${varCharValueColumn("channel")},
                ${varCharValueColumn("user_id")},
                ${varCharValueColumn("user_name")},
                ${varCharValueColumn("contact_date")},
                ${varCharValueColumn("contact_time")},
                ${varCharValueColumn("modified_date")},
                ${varCharValueColumn("modified_time")}
              ]
            },
            ${contactEventData(firstId)},
            ${contactEventData("123456789")}
          ],
          "ResultSetMetadata": {
            "ColumnInfo": [
              ${metaDataRow("legacy_subject_id")},
              ${metaDataRow("legacy_order_id")},
              ${metaDataRow("contact_type")},
              ${metaDataRow("reason")},
              ${metaDataRow("channel")},
              ${metaDataRow("user_id")},
              ${metaDataRow("user_name")},
              ${metaDataRow("contact_date")},
              ${metaDataRow("contact_time")},
              ${metaDataRow("modified_date")},
              ${metaDataRow("modified_time")}
            ]
          }
        },
        "UpdateCount": 0
      }
    """.trimIndent()

    @Test
    fun `getContactEventsList passes correct query to getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(contactEventsResultSet())

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      repository.getContactEventsList("123", AthenaRole.DEV)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getContactEventsList returns an AthenaContactEventListDTO`() {
      val resultSet = AthenaHelper.resultSetFromJson(contactEventsResultSet())

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getContactEventsList("123", AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(AthenaContactEventListDTO::class.java)
    }

    @Test
    fun `getContactEventsList returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(contactEventsResultSet("987"))

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getContactEventsList("987", AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.pageSize).isEqualTo(2)
      Assertions.assertThat(result.events.first().legacySubjectId).isEqualTo(987)
      Assertions.assertThat(result.events.first().legacyOrderId).isEqualTo(987)
      Assertions.assertThat(result.events.first().contactType).isEqualTo("TEST_CONTACT")
    }
  }
}
