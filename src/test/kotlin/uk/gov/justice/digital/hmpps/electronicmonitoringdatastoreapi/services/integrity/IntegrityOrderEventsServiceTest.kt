package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.integrity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Event
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityContactEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityMonitoringEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityViolationEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IncidentEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityMonitoringEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityViolationEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityOrderEventsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity.IntegrityOrderEventsService

class IntegrityOrderEventsServiceTest {
  private lateinit var integrityOrderEventsRepository: IntegrityOrderEventsRepository
  private lateinit var service: IntegrityOrderEventsService

  @BeforeEach
  fun setup() {
    integrityOrderEventsRepository = Mockito.mock(IntegrityOrderEventsRepository::class.java)
    service = IntegrityOrderEventsService(integrityOrderEventsRepository)
  }

  @Test
  fun `OrderService can be instantiated`() {
    val sut = IntegrityOrderEventsService(integrityOrderEventsRepository)
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetMonitoringEventsList {
    val legacySubjectId = "fake-id"

    val exampleMonitoringEventList = listOf(
      AthenaIntegrityMonitoringEventDTO(
        legacySubjectId = "123",
        eventType = "TEST_EVENT",
        eventDate = "2022-10-10",
        eventTime = "13:00:05",
        eventSecond = 5,
        processDate = "2025-01-08",
        processTime = "09:10:45",
        processSecond = 10,
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(integrityOrderEventsRepository.getMonitoringEventsList(legacySubjectId, false))
        .thenReturn(exampleMonitoringEventList)
    }

    @Test
    fun `calls getMonitoringEventsList from order information repository`() {
      service.getMonitoringEvents(legacySubjectId, false)

      Mockito.verify(integrityOrderEventsRepository, Mockito.times(1)).getMonitoringEventsList(legacySubjectId, false)
    }

    @Test
    fun `returns MonitoringEventList when a response is received`() {
      val result = service.getMonitoringEvents(legacySubjectId, false)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the order when a response is received`() {
      val result = service.getMonitoringEvents(legacySubjectId, false)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(Event::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")

      Assertions.assertThat(result.first().details).isInstanceOf(IntegrityMonitoringEventDetails::class.java)
      Assertions.assertThat(result.first().details.type).isEqualTo("TEST_EVENT")
    }
  }

  @Nested
  inner class GetIncidentEvents {
    val legacySubjectId = "fake-id"

    val exampleIncidentEventList = listOf(
      AthenaIntegrityIncidentEventDTO(
        legacySubjectId = "123",
        violationAlertType = "TEST_VIOLATION",
        violationAlertDate = "2019-06-24",
        violationAlertTime = "05:38:23",
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(integrityOrderEventsRepository.getIncidentEventsList(legacySubjectId, false))
        .thenReturn(exampleIncidentEventList)
    }

    @Test
    fun `calls getIncidentEventsList from order information repository`() {
      service.getIncidentEvents(legacySubjectId, false)

      Mockito.verify(integrityOrderEventsRepository, Mockito.times(1)).getIncidentEventsList(legacySubjectId, false)
    }

    @Test
    fun `returns IncidentEventList when a response is received`() {
      val result = service.getIncidentEvents(legacySubjectId, false)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the order when a response is received`() {
      val result = service.getIncidentEvents(legacySubjectId, false)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(Event::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")

      Assertions.assertThat(result.first().details).isInstanceOf(IncidentEventDetails::class.java)
      Assertions.assertThat(result.first().details.type).isEqualTo("TEST_VIOLATION")
    }
  }

  @Nested
  inner class GetViolationEvents {
    val legacySubjectId = "fake-id"

    val exampleViolationEventList = listOf(
      AthenaIntegrityViolationEventDTO(
        legacySubjectId = "123",
        enforcementReason = "TEST_REASON",
        investigationOutcomeReason = "TEST_OUTCOME",
        breachDetails = "TEST_BREACH",
        breachEnforcementOutcome = "TEST_OUTCOME",
        agencyAction = "no action",
        breachDate = "2022-10-10",
        breachTime = "10:10:10",
        breachIdentifiedDate = "2022-11-11",
        breachIdentifiedTime = "11:11:11",
        authorityFirstNotifiedDate = "2022-12-12",
        authorityFirstNotifiedTime = "12:12:12",
        agencyResponseDate = "2023-01-13",
        breachPackRequestedDate = "2023-02-14",
        breachPackSentDate = "2023-03-15",
        section9Date = "2023-04-16",
        hearingDate = "2023-05-17",
        summonsServedDate = "2023-06-18",
        subjectLetterSentDate = "2023-07-18",
        warningLetterSentDate = "2023-08-19",
        warningLetterSentTime = "19:19:19",
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(integrityOrderEventsRepository.getViolationEventsList(legacySubjectId, false))
        .thenReturn(exampleViolationEventList)
    }

    @Test
    fun `calls getViolationEventsList from order information repository`() {
      service.getViolationEvents(legacySubjectId, false)

      Mockito.verify(integrityOrderEventsRepository, Mockito.times(1)).getViolationEventsList(legacySubjectId, false)
    }

    @Test
    fun `returns ViolationEventList when a response is received`() {
      val result = service.getViolationEvents(legacySubjectId, false)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the order when a response is received`() {
      val result = service.getViolationEvents(legacySubjectId, false)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(Event::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")

      Assertions.assertThat(result.first().details).isInstanceOf(IntegrityViolationEventDetails::class.java)
      Assertions.assertThat(result.first().details.breachDetails).isEqualTo("TEST_BREACH")
    }
  }

  @Nested
  inner class GetContactEvents {
    val legacySubjectId = "fake-id"

    val exampleContactEventList = listOf(
      AthenaIntegrityContactEventDTO(
        legacySubjectId = "123",
        outcome = "Mr silly laughed out loud",
        contactType = "TEST_CONTACT",
        reason = "A silly reason",
        channel = "Tickles",
        userId = "",
        userName = "",
        contactDate = "2547-03-12",
        contactTime = "04:27:24",
        modifiedDate = "2135-12-31",
        modifiedTime = "12:34:12",
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(integrityOrderEventsRepository.getContactEventsList(legacySubjectId, false))
        .thenReturn(exampleContactEventList)
    }

    @Test
    fun `calls getContactEventsList from order information repository`() {
      service.getContactEvents(legacySubjectId, false)

      Mockito.verify(integrityOrderEventsRepository, Mockito.times(1)).getContactEventsList(legacySubjectId, false)
    }

    @Test
    fun `returns ContactsEventList when a response is received`() {
      val result = service.getContactEvents(legacySubjectId, false)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the order when a response is received`() {
      val result = service.getContactEvents(legacySubjectId, false)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(Event::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")

      Assertions.assertThat(result.first().details).isInstanceOf(IntegrityContactEventDetails::class.java)
      Assertions.assertThat(result.first().details.type).isEqualTo("TEST_CONTACT")
    }
  }
}
