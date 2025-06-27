package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmEvent
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmIncidentEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmViolationEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmContactEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmViolationEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmOrderEventsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring.AmOrderEventsService

class AmOrderEventsServiceTest {
  private lateinit var amOrderEventsRepository: AmOrderEventsRepository
  private lateinit var service: AmOrderEventsService

  @BeforeEach
  fun setup() {
    amOrderEventsRepository = Mockito.mock(AmOrderEventsRepository::class.java)
    service = AmOrderEventsService(amOrderEventsRepository)
  }

  @Test
  fun `OrderService can be instantiated`() {
    val sut = AmOrderEventsService(amOrderEventsRepository)
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetContactEvents {
    val legacySubjectId = "fake-id"

    val exampleContactEventList = listOf(
      AthenaAmContactEventDTO(
        legacySubjectId = "123",
        contactDate = "2001-01-01",
        contactTime = "01:01:01",
        inboundOrOutbound = "INBOUND",
        fromTo = "FROM",
        channel = "TEST_CHANNEL",
        subjectConsentWithdrawn = "NO",
        callOutcome = "TEST_OUTCOME",
        statement = "TEST_STATEMENT",
        reasonForContact = "TEST_REASON",
        outcomeOfContact = "TEST_OUTCOME",
        visitRequired = "NO",
        visitId = "V123",
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(amOrderEventsRepository.getContactEventsList(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO))
        .thenReturn(exampleContactEventList)
    }

    @Test
    fun `calls getContactEventsList from AM order information repository`() {
      service.getContactEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Mockito.verify(amOrderEventsRepository, Mockito.times(1)).getContactEventsList(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)
    }

    @Test
    fun `returns a list of Event when a response is received`() {
      val result = service.getContactEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
      Assertions.assertThat(result).allSatisfy {
        Assertions.assertThat(it).isInstanceOf(AmEvent::class.java)
      }
    }

    @Test
    fun `Each Event contains AmContactEventDetails`() {
      val result = service.getContactEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Assertions.assertThat(result).allSatisfy {
        Assertions.assertThat(it.details).isInstanceOf(AmContactEventDetails::class.java)
      }
    }

    @Test
    fun `returns correct details of the order when a response is received`() {
      val result = service.getContactEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(AmEvent::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")

      Assertions.assertThat(result.first().details).isInstanceOf(AmContactEventDetails::class.java)
      Assertions.assertThat(result.first().details.channel).isEqualTo("TEST_CHANNEL")
      Assertions.assertThat(result.first().details.contactDateTime).isEqualTo("2001-01-01T01:01:01")
    }
  }

  @Nested
  inner class GetAmIncidentEvents {
    val legacySubjectId = "fake-id"

    val exampleAmIncidentEventList = listOf(
      AthenaAmIncidentEventDTO(
        legacySubjectId = "123",
        violationAlertId = "V123",
        violationAlertDate = "2001-01-01",
        violationAlertTime = "01:01:01",
        violationAlertType = "TEST_ALERT_TYPE",
        violationAlertResponseAction = "TEST_RESPONSE_ACTION",
        visitRequired = "Yes",
        probationInteractionRequired = "No",
        amsInteractionRequired = "Yes",
        multipleAlerts = "No",
        additionalAlerts = "NO",
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(amOrderEventsRepository.getIncidentEventsList(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO))
        .thenReturn(exampleAmIncidentEventList)
    }

    @Test
    fun `calls getIncidentEventsList from AM order events repository`() {
      service.getIncidentEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Mockito.verify(amOrderEventsRepository, Mockito.times(1)).getIncidentEventsList(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)
    }

    @Test
    fun `returns a list of Event when a response is received`() {
      val result = service.getIncidentEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
      Assertions.assertThat(result).allSatisfy {
        Assertions.assertThat(it).isInstanceOf(AmEvent::class.java)
      }
    }

    @Test
    fun `Each Event contains AmIncidentEventDetails`() {
      val result = service.getIncidentEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Assertions.assertThat(result).allSatisfy {
        Assertions.assertThat(it.details).isInstanceOf(AmIncidentEventDetails::class.java)
      }
    }

    @Test
    fun `Returns correct details of the order when a response is received`() {
      val result = service.getIncidentEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(AmEvent::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")

      Assertions.assertThat(result.first().details).isInstanceOf(AmIncidentEventDetails::class.java)
      Assertions.assertThat(result.first().details.violationAlertType).isEqualTo("TEST_ALERT_TYPE")
      Assertions.assertThat(result.first().details.violationAlertDateTime).isEqualTo("2001-01-01T01:01:01")
    }
  }

  @Nested
  inner class GetViolationEvents {
    val legacySubjectId = "fake-id"

    val exampleViolationEventList = listOf(
      AthenaAmViolationEventDTO(
        legacySubjectId = "123",
        enforcementId = "E123",
        nonComplianceReason = "TEST_REASON",
        nonComplianceDate = "2001-01-01",
        nonComplianceTime = "01:01:01",
        violationAlertId = "V123",
        violationAlertDescription = "TEST_DESCRIPTION",
        violationEventNotificationDate = "2002-02-02",
        violationEventNotificationTime = "02:02:02",
        actionTakenEms = "TEST_ACTION",
        nonComplianceOutcome = "TEST_OUTCOME",
        nonComplianceResolved = "TEST_RESOLVED",
        dateResolved = "2003-03-03",
        openClosed = "CLOSED",
        visitRequired = "NO",
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(amOrderEventsRepository.getViolationEventsList(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO))
        .thenReturn(exampleViolationEventList)
    }

    @Test
    fun `Calls getViolationEventsList from AM order events repository`() {
      service.getViolationEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Mockito.verify(amOrderEventsRepository, Mockito.times(1)).getViolationEventsList(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)
    }

    @Test
    fun `returns a list of Event when a response is received`() {
      val result = service.getViolationEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
      Assertions.assertThat(result).allSatisfy {
        Assertions.assertThat(it).isInstanceOf(AmEvent::class.java)
      }
    }

    @Test
    fun `Each Event contains AmViolationEventDetails`() {
      val result = service.getViolationEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Assertions.assertThat(result).allSatisfy {
        Assertions.assertThat(it.details).isInstanceOf(AmViolationEventDetails::class.java)
      }
    }

    @Test
    fun `returns correct details of the order when a response is received`() {
      val result = service.getViolationEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(AmEvent::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")

      Assertions.assertThat(result.first().details).isInstanceOf(AmViolationEventDetails::class.java)
      Assertions.assertThat(result.first().details.nonComplianceReason).isEqualTo("TEST_REASON")
      Assertions.assertThat(result.first().details.nonComplianceDateTime).isEqualTo("2001-01-01T01:01:01")
    }
  }
}
