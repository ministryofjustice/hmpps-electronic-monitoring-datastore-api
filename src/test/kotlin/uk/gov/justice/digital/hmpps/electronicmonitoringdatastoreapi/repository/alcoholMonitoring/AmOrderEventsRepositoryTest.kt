package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.mocks.MockAthenaResultSetBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmContactEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmViolationEventDTO

class AmOrderEventsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: AmOrderEventsRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = AmOrderEventsRepository(emDatastoreClient)
  }

  @Test
  fun `AmOrderEventsRepository can be instantiated`() {
    val sut = AmOrderEventsRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetIncidentEventsList {
    fun amIncidentEventsResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "violation_alert_id",
        "violation_alert_date",
        "violation_alert_time",
        "violation_alert_type",
        "violation_alert_response_action",
        "visit_required",
        "probation_interaction_required",
        "ams_interaction_required",
        "multiple_alerts",
        "additional_alerts",
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
    ).build()

    @Test
    fun `getIncidentEventsList passes correct query to getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amIncidentEventsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      repository.getIncidentEventsList("123")

      Mockito.verify(emDatastoreClient).getQueryResult(any<SqlQueryBuilder>(), eq(false))
    }

    @Test
    fun `getIncidentEventsList returns a list of AthenaIncidentEventDTO`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amIncidentEventsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      val result = repository.getIncidentEventsList("123")

      Assertions.assertThat(result).isInstanceOf(List::class.java)
      Assertions.assertThat(result).allSatisfy {
        Assertions.assertThat(it).isInstanceOf(AthenaAmIncidentEventDTO::class.java)
      }
    }

    @Test
    fun `getIncidentEventsList returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amIncidentEventsResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      val result = repository.getIncidentEventsList("987")

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(2)

      Assertions.assertThat(result.first()).isInstanceOf(AthenaAmIncidentEventDTO::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
      Assertions.assertThat(result.first().violationAlertType).isEqualTo("TEST_ALERT_1")
    }
  }

  @Nested
  inner class GetContactEventsList {
    fun amContactEventsResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "contact_date",
        "contact_time",
        "inbound_or_outbound",
        "from_to",
        "channel",
        "subject_consent_withdrawn",
        "call_outcome",
        "statement",
        "reason_for_contact",
        "outcome_of_contact",
        "visit_required",
        "visit_id",
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
    ).build()

    @Test
    fun `getContactEventsList passes correct query to getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amContactEventsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      repository.getContactEventsList("123")

      Mockito.verify(emDatastoreClient).getQueryResult(any<SqlQueryBuilder>(), eq(false))
    }

    @Test
    fun `getContactEventsList returns a list of AthenaContactEventDTO`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amContactEventsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      val result = repository.getContactEventsList("123")

      Assertions.assertThat(result).isInstanceOf(List::class.java)
      Assertions.assertThat(result).allSatisfy {
        Assertions.assertThat(it).isInstanceOf(AthenaAmContactEventDTO::class.java)
      }
    }

    @Test
    fun `getContactEventsList returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amContactEventsResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      val result = repository.getContactEventsList("987")

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(2)

      Assertions.assertThat(result.first()).isInstanceOf(AthenaAmContactEventDTO::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
      Assertions.assertThat(result.first().inboundOrOutbound).isEqualTo("Inbound")
      Assertions.assertThat(result.first().channel).isEqualTo("Call")
    }
  }

  @Nested
  inner class GetViolationEventsList {
    fun amViolationEventsResultSet(firstId: String = "987123") = MockAthenaResultSetBuilder(
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
    ).build()

    @Test
    fun `getViolationEventsList passes correct query to getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amViolationEventsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      repository.getViolationEventsList("123")

      Mockito.verify(emDatastoreClient).getQueryResult(any<SqlQueryBuilder>(), eq(false))
    }

    @Test
    fun `getViolationEventsList returns a list of AthenaViolationEventDTO`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amViolationEventsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      val result = repository.getViolationEventsList("123")

      Assertions.assertThat(result).isInstanceOf(List::class.java)
      Assertions.assertThat(result).allSatisfy {
        Assertions.assertThat(it).isInstanceOf(AthenaAmViolationEventDTO::class.java)
      }
    }

    @Test
    fun `getViolationEventsList returns all the results from getQueryResult`() {
      val resultSet = AthenaHelper.Companion.resultSetFromJson(amViolationEventsResultSet("987"))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      val result = repository.getViolationEventsList("987")

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(2)

      Assertions.assertThat(result.first()).isInstanceOf(AthenaAmViolationEventDTO::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("987")
      Assertions.assertThat(result.first().violationAlertId).isEqualTo("V123")
      Assertions.assertThat(result.first().nonComplianceDate).isEqualTo("2001-01-01")
    }
  }
}
