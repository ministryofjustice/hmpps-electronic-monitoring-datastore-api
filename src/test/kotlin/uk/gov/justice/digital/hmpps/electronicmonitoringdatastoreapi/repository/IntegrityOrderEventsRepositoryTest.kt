package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.queryBuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder
import java.util.UUID

class IntegrityOrderEventsRepositoryTest {
  private lateinit var athenaClient: EmDatastoreClient
  private lateinit var repository: IntegrityOrderEventsRepository

  @BeforeEach
  fun setup() {
    athenaClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = IntegrityOrderEventsRepository(athenaClient)
  }

  @Nested
  inner class GetMonitoringEventsList {
    fun mockResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
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
          "TEST_EVENT",
          "2001-01-01",
          "01:01:01",
          "1",
          "2002-02-02",
          "02:02:02",
          "2",
        ),
      ),
    ).toResultSet()

    @Test
    fun `getMonitoringEventsList passes correct query to getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet())

      repository.getMonitoringEventsList("123", false)

      Mockito.verify(athenaClient).getQueryResult(queryExecutionId, false)
    }

    @Test
    fun `getMonitoringEventsList returns all the results from getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet("987"))

      val result = repository.getMonitoringEventsList("987", false)

      Assertions.assertThat(result.size).isEqualTo(2)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
      Assertions.assertThat(result.first().eventType).isEqualTo("TEST_EVENT")
    }
  }

  @Nested
  inner class GetIncidentEventsList {
    fun mockResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "violation_alert_type",
        "violation_alert_date",
        "violation_alert_time",
      ),
      rows = arrayOf(
        arrayOf(
          firstId,
          "TEST_ALERT",
          "2001-01-01",
          "01:01:01",
        ),
        arrayOf(
          "123456789",
          "TEST_ALERT",
          "2001-01-01",
          "01:01:01",
        ),
      ),
    ).toResultSet()

    @Test
    fun `getIncidentEventsList passes correct query to getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet())

      repository.getIncidentEventsList("123", false)

      Mockito.verify(athenaClient).getQueryResult(queryExecutionId, false)
    }

    @Test
    fun `getIncidentEventsList returns all the results from getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet("987"))

      val result = repository.getIncidentEventsList("987", false)

      Assertions.assertThat(result.size).isEqualTo(2)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
      Assertions.assertThat(result.first().violationAlertType).isEqualTo("TEST_ALERT")
    }
  }

  @Nested
  inner class GetViolationEventsList {
    fun mockResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
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
    ).toResultSet()

    @Test
    fun `getViolationEventsList passes correct query to getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet())

      repository.getViolationEventsList("123", false)

      Mockito.verify(athenaClient).getQueryResult(queryExecutionId, false)
    }

    @Test
    fun `getViolationEventsList returns all the results from getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet("987"))

      val result = repository.getViolationEventsList("987", false)

      Assertions.assertThat(result.size).isEqualTo(2)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
      Assertions.assertThat(result.first().investigationOutcomeReason).isEqualTo("TEST_OUTCOME_REASON")
    }
  }

  @Nested
  inner class GetContactEventsList {
    fun mockResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
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
    ).toResultSet()

    @Test
    fun `getContactEventsList passes correct query to getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet())

      repository.getContactEventsList("123", false)

      Mockito.verify(athenaClient).getQueryResult(queryExecutionId, false)
    }

    @Test
    fun `getContactEventsList returns all the results from getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet("987"))

      val result = repository.getContactEventsList("987", false)

      Assertions.assertThat(result.size).isEqualTo(2)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
      Assertions.assertThat(result.first().contactType).isEqualTo("TEST_CONTACT")
    }
  }
}
