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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.mocks.MockAthenaResultSetBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaViolationEventDTO

class OrderEventsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: OrderEventsRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = OrderEventsRepository(emDatastoreClient)
  }

  @Test
  fun `OrderEventsRepository can be instantiated`() {
    val sut = OrderEventsRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetMonitoringEventsList {
    fun monitoringEventsResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "legacy_order_id",
        "event_type",
        "event_date",
        "event_time",
        "event_second",
        "process_date",
        "process_time",
        "process_second",
      ),
      rows = arrayOf(
        arrayOf(
          firstId,
          firstId,
          "TEST_EVENT",
          "2001-01-01",
          "01:01:01",
          "1",
          "2002-02-02",
          "02:02:02",
          "2",
        ),
        arrayOf(
          "123456789",
          "123456789",
          "TEST_EVENT",
          "2001-01-01",
          "01:01:01",
          "1",
          "2002-02-02",
          "02:02:02",
          "2",
        ),
      ),
    ).build()

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

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `getMonitoringEventsList returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(monitoringEventsResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getMonitoringEventsList("987", AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(2)

      Assertions.assertThat(result.first()).isInstanceOf(AthenaMonitoringEventDTO::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo(987)
      Assertions.assertThat(result.first().legacyOrderId).isEqualTo(987)
      Assertions.assertThat(result.first().eventType).isEqualTo("TEST_EVENT")
    }
  }

  @Nested
  inner class GetIncidentEventsList {
    fun incidentEventsResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "legacy_order_id",
        "violation_alert_type",
        "violation_alert_date",
        "violation_alert_time",
      ),
      rows = arrayOf(
        arrayOf(
          firstId,
          firstId,
          "TEST_ALERT",
          "2001-01-01",
          "01:01:01",
        ),
        arrayOf(
          "123456789",
          "123456789",
          "TEST_ALERT",
          "2001-01-01",
          "01:01:01",
        ),
      ),
    ).build()

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

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `getIncidentEventsList returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(incidentEventsResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getIncidentEventsList("987", AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(2)

      Assertions.assertThat(result.first()).isInstanceOf(AthenaIncidentEventDTO::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo(987)
      Assertions.assertThat(result.first().legacyOrderId).isEqualTo(987)
      Assertions.assertThat(result.first().violationAlertType).isEqualTo("TEST_ALERT")
    }
  }

  @Nested
  inner class GetViolationEventsList {
    fun violationEventsResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "legacy_order_id",
        "enforcement_reason",
        "investigation_outcome_reason",
        "breach_details",
        "breach_enforcement_outcome",
        "agency_action",
        "breach_date",
        "breach_time",
        "breach_identified_date",
        "breach_identified_time",
        "authority_first_notified_date",
        "authority_first_notified_time",
        "agency_response_date",
        "breach_pack_requested_date",
        "breach_pack_sent_date",
        "section_9_date",
        "hearing_date",
        "summons_served_date",
        "subject_letter_sent_date",
        "warning_letter_sent_date",
        "warning_letter_sent_time",
      ),
      rows = arrayOf(
        arrayOf(
          firstId,
          firstId,
          "TEST_ENFORCEMENT_REASON",
          "TEST_OUTCOME_REASON",
          "some details",
          "TEST_OUTCOME_REASON",
          "TEST_ACTION",
          "2003-03-03",
          "03:03:03",
          "2004-04-04",
          "04:04:04",
          "2005-05-05",
          "05:05:05",
          "2006-06-06",
          "06:06:06",
          "2007-07-07",
          "2008-08-08",
          "2009-09-09",
          "2010-10-10",
          "2011-11-11",
          "2012-12-12",
          "12:12:12",
        ),
        arrayOf(
          "123456789",
          "123456789",
          "TEST_ENFORCEMENT_REASON",
          "TEST_OUTCOME_REASON",
          "some details",
          "TEST_OUTCOME_REASON",
          "TEST_ACTION",
          "2003-03-03",
          "03:03:03",
          "2004-04-04",
          "04:04:04",
          "2005-05-05",
          "05:05:05",
          "2006-06-06",
          "06:06:06",
          "2007-07-07",
          "2008-08-08",
          "2009-09-09",
          "2010-10-10",
          "2011-11-11",
          "2012-12-12",
          "12:12:12",
        ),
      ),
    ).build()

    @Test
    fun `getViolationEventsList passes correct query to getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(violationEventsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      repository.getViolationEventsList("123", AthenaRole.DEV)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getViolationEventsList returns an AthenaViolationEventListDTO`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(violationEventsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getViolationEventsList("123", AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `getViolationEventsList returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(violationEventsResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getViolationEventsList("987", AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(2)

      Assertions.assertThat(result.first()).isInstanceOf(AthenaViolationEventDTO::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo(987)
      Assertions.assertThat(result.first().legacyOrderId).isEqualTo(987)
      Assertions.assertThat(result.first().investigationOutcomeReason).isEqualTo("TEST_OUTCOME_REASON")
    }
  }

  @Nested
  inner class GetContactEventsList {
    fun contactEventsResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "legacy_order_id",
        "contact_type",
        "reason",
        "channel",
        "user_id",
        "user_name",
        "contact_date",
        "contact_time",
        "modified_date",
        "modified_time",
      ),
      rows = arrayOf(
        arrayOf(
          firstId,
          firstId,
          "TEST_CONTACT",
          "No reason",
          "PHONE_CALL",
          "usr_001",
          "Bob the Builder",
          "2001-01-01",
          "01:01:01",
          "2002-02-02",
          "02:02:02",
        ),
        arrayOf(
          "123456789",
          "123456789",
          "TEST_CONTACT",
          "No reason",
          "PHONE_CALL",
          "usr_001",
          "Bob the Builder",
          "2001-01-01",
          "01:01:01",
          "2002-02-02",
          "02:02:02",
        ),
      ),
    ).build()

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

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `getContactEventsList returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(contactEventsResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getContactEventsList("987", AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(2)

      Assertions.assertThat(result.first()).isInstanceOf(AthenaContactEventDTO::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo(987)
      Assertions.assertThat(result.first().legacyOrderId).isEqualTo(987)
      Assertions.assertThat(result.first().contactType).isEqualTo("TEST_CONTACT")
    }
  }
}
