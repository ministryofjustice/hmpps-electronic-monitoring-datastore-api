package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery

class OrderEventsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: OrderEventsRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = OrderEventsRepository(emDatastoreClient)
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

  @Test
  fun `OrderEventsRepository can be instantiated`() {
    val sut = OrderInformationRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
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
      val resultSet = AthenaHelper.Companion.resultSetFromJson(monitoringEventsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      repository.getMonitoringEventsList("123", AthenaRole.DEV)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getMonitoringEventsList returns an AthenaMonitoringEventListDTO`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(monitoringEventsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getMonitoringEventsList("123", AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(AthenaMonitoringEventListDTO::class.java)
    }

    @Test
    fun `getMonitoringEventsList returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(monitoringEventsResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

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
      val resultSet = AthenaHelper.Companion.resultSetFromJson(incidentEventsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      repository.getIncidentEventsList("123", AthenaRole.DEV)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getIncidentEventsList returns an AthenaIncidentEventListDTO`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(incidentEventsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getIncidentEventsList("123", AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(AthenaIncidentEventListDTO::class.java)
    }

    @Test
    fun `getIncidentEventsList returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(incidentEventsResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

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
      val resultSet = AthenaHelper.Companion.resultSetFromJson(contactEventsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      repository.getContactEventsList("123", AthenaRole.DEV)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getContactEventsList returns an AthenaContactEventListDTO`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(contactEventsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getContactEventsList("123", AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(AthenaContactEventListDTO::class.java)
    }

    @Test
    fun `getContactEventsList returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(contactEventsResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getContactEventsList("987", AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.pageSize).isEqualTo(2)
      Assertions.assertThat(result.events.first().legacySubjectId).isEqualTo(987)
      Assertions.assertThat(result.events.first().legacyOrderId).isEqualTo(987)
      Assertions.assertThat(result.events.first().contactType).isEqualTo("TEST_CONTACT")
    }
  }
}
