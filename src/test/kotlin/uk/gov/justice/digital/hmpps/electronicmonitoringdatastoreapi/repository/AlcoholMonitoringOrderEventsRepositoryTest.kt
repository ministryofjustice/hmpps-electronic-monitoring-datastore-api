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

class AlcoholMonitoringOrderEventsRepositoryTest {
  private lateinit var athenaClient: EmDatastoreClient
  private lateinit var repository: AlcoholMonitoringOrderEventsRepository

  @BeforeEach
  fun setup() {
    athenaClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = AlcoholMonitoringOrderEventsRepository(athenaClient)
  }

  @Nested
  inner class GetIncidentEventsList {
    fun mockResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = mapOf(
        "legacy_subject_id" to "varchar",
        "violation_alert_id" to "varchar",
        "violation_alert_date" to "varchar",
        "violation_alert_time" to "varchar",
        "violation_alert_type" to "varchar",
        "violation_alert_response_action" to "varchar",
        "visit_required" to "varchar",
        "probation_interaction_required" to "varchar",
        "ams_interaction_required" to "varchar",
        "multiple_alerts" to "varchar",
        "additional_alerts" to "varchar",
      ),
      rows = arrayOf(
        arrayOf(
          firstId,
          firstId,
          "2001-01-01",
          "01:01:01",
          "TEST_ALERT_1",
          "Violation alert response action",
          "Yes",
          "No",
          "Yes",
          "No",
          "No",
        ),
        arrayOf(
          "123456789",
          "123456789",
          "2002-02-02",
          "02:02:02",
          "TEST_ALERT_2",
          "Violation alert response action",
          "No",
          "Yes",
          "No",
          "Yes",
          "Yes",
        ),
      ),
    ).toResultSet()

    @Test
    fun `passes correct query to getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet())

      repository.getIncidentEventsList("123")

      Mockito.verify(athenaClient).getQueryResult(queryExecutionId, false)
    }

    @Test
    fun `returns all the results from getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet("987"))

      val result = repository.getIncidentEventsList("987")

      Assertions.assertThat(result.size).isEqualTo(2)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
      Assertions.assertThat(result.first().violationAlertType).isEqualTo("TEST_ALERT_1")
    }
  }

  @Nested
  inner class GetContactEventsList {
    fun mockResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = mapOf(
        "legacy_subject_id" to "varchar",
        "contact_date" to "varchar",
        "contact_time" to "varchar",
        "inbound_or_outbound" to "varchar",
        "from_to" to "varchar",
        "channel" to "varchar",
        "subject_consent_withdrawn" to "varchar",
        "call_outcome" to "varchar",
        "statement" to "varchar",
        "reason_for_contact" to "varchar",
        "outcome_of_contact" to "varchar",
        "visit_required" to "varchar",
        "visit_id" to "varchar",
      ),
      rows = arrayOf(
        arrayOf(
          firstId,
          "2001-01-01",
          "01:01:01",
          "Inbound",
          "Device wearer",
          "Call",
          "Call outcome",
          "Statement",
          "Contact reason",
          "Contact outcome",
          "No",
          "1234567",
        ),
        arrayOf(
          "123456789",
          "2002-02-02",
          "02:02:02",
          "Outbound",
          "Probation",
          "Call",
          "Call outcome",
          "Statement",
          "Contact reason",
          "Contact outcome",
          "Yes",
          "7654321",
        ),
      ),
    ).toResultSet()

    @Test
    fun `passes correct query to getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet())

      repository.getContactEventsList("123")

      Mockito.verify(athenaClient).getQueryResult(queryExecutionId, false)
    }

    @Test
    fun `returns all the results from getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet("987"))

      val result = repository.getContactEventsList("987")

      Assertions.assertThat(result.size).isEqualTo(2)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
      Assertions.assertThat(result.first().inboundOrOutbound).isEqualTo("Inbound")
      Assertions.assertThat(result.first().channel).isEqualTo("Call")
    }
  }

  @Nested
  inner class GetViolationEventsList {
    fun mockResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "enforcement_id",
        "non_compliance_reason",
        "non_compliance_date",
        "non_compliance_time",
        "violation_alert_id",
        "violation_alert_description",
        "violation_event_notification_date",
        "violation_event_notification_time",
        "action_taken_ems",
        "non_compliance_outcome",
        "non_compliance_resolved",
        "date_resolved",
        "open_closed",
        "visit_required",
      ),
      rows = arrayOf(
        arrayOf(
          firstId,
          firstId,
          "Noncompliance reason",
          "2001-01-01",
          "01:01:01",
          "V123",
          "Violation alert description",
          "2002-02-02",
          "02:02:02",
          "Action taken EMS",
          "Outcome",
          "Yes",
          "2003-03-03",
          "Closed",
          "No",
        ),
        arrayOf(
          "123456789",
          "123456789",
          "Noncompliance reason",
          "2004-04-04",
          "04:04:04",
          "V321",
          "Violation alert description",
          "2005-05-05",
          "05:05:05",
          "Action taken EMS",
          "Outcome",
          "No",
          "2006-06-06",
          "Open",
          "Yes",
        ),
      ),
    ).toResultSet()

    @Test
    fun `passes correct query to getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet())

      repository.getViolationEventsList("123")

      Mockito.verify(athenaClient).getQueryResult(queryExecutionId, false)
    }

    @Test
    fun `returns all the results from getQueryResult`() {
      val queryExecutionId = UUID.randomUUID().toString()
      Mockito.`when`(athenaClient.getQueryExecutionId(any<SqlQueryBuilder>(), eq(false)))
        .thenReturn(queryExecutionId)
      Mockito.`when`(athenaClient.getQueryResult(eq(queryExecutionId), eq(false)))
        .thenReturn(mockResultSet("987"))

      val result = repository.getViolationEventsList("987")

      Assertions.assertThat(result.size).isEqualTo(2)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
      Assertions.assertThat(result.first().violationAlertId).isEqualTo("V123")
      Assertions.assertThat(result.first().nonComplianceDate).isEqualTo("2001-01-01")
    }
  }
}
