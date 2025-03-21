package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.ContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Event
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.IncidentEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.MonitoringEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.ViolationEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaViolationEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderEventsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderEventsService

class OrderEventsServiceTest {
  private lateinit var orderEventsRepository: OrderEventsRepository
  private lateinit var service: OrderEventsService

  @BeforeEach
  fun setup() {
    orderEventsRepository = Mockito.mock(OrderEventsRepository::class.java)
    service = OrderEventsService(orderEventsRepository)
  }

  @Test
  fun `OrderService can be instantiated`() {
    val sut = OrderEventsService(orderEventsRepository)
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetMonitoringEventsList {
    val legacySubjectId = "fake-id"

    val exampleMonitoringEventList = listOf<AthenaMonitoringEventDTO>(
      AthenaMonitoringEventDTO(
        legacySubjectId = 123,
        legacyOrderId = 123,
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
      Mockito.`when`(orderEventsRepository.getMonitoringEventsList(legacySubjectId, false))
        .thenReturn(exampleMonitoringEventList)
    }

    @Test
    fun `calls getMonitoringEventsList from order information repository`() {
      service.getMonitoringEvents(legacySubjectId, false)

      Mockito.verify(orderEventsRepository, Mockito.times(1)).getMonitoringEventsList(legacySubjectId, false)
    }

    @Test
    fun `returns MonitoringEventList when a response is received`() {
      var result = service.getMonitoringEvents(legacySubjectId, false)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the order when a response is received`() {
      var result = service.getMonitoringEvents(legacySubjectId, false)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(Event::class.java)
      Assertions.assertThat(result.first().legacyOrderId).isEqualTo(123)

      Assertions.assertThat(result.first().details).isInstanceOf(MonitoringEventDetails::class.java)
      Assertions.assertThat(result.first().details.type).isEqualTo("TEST_EVENT")
    }
  }

  @Nested
  inner class GetIncidentEvents {
    val legacySubjectId = "fake-id"

    val exampleIncidentEventList = listOf<AthenaIncidentEventDTO>(
      AthenaIncidentEventDTO(
        legacySubjectId = 123,
        legacyOrderId = 123,
        violationAlertType = "TEST_VIOLATION",
        violationAlertDate = "2019-06-24",
        violationAlertTime = "05:38:23",
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(orderEventsRepository.getIncidentEventsList(legacySubjectId, false))
        .thenReturn(exampleIncidentEventList)
    }

    @Test
    fun `calls getIncidentEventsList from order information repository`() {
      service.getIncidentEvents(legacySubjectId, false)

      Mockito.verify(orderEventsRepository, Mockito.times(1)).getIncidentEventsList(legacySubjectId, false)
    }

    @Test
    fun `returns IncidentEventList when a response is received`() {
      var result = service.getIncidentEvents(legacySubjectId, false)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the order when a response is received`() {
      var result = service.getIncidentEvents(legacySubjectId, false)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(Event::class.java)
      Assertions.assertThat(result.first().legacyOrderId).isEqualTo(123)

      Assertions.assertThat(result.first().details).isInstanceOf(IncidentEventDetails::class.java)
      Assertions.assertThat(result.first().details.type).isEqualTo("TEST_VIOLATION")
    }
  }

  @Nested
  inner class GetViolationEvents {
    val legacySubjectId = "fake-id"

    val exampleViolationEventList = listOf<AthenaViolationEventDTO>(
      AthenaViolationEventDTO(
        legacySubjectId = 123,
        legacyOrderId = 123,
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
      Mockito.`when`(orderEventsRepository.getViolationEventsList(legacySubjectId, false))
        .thenReturn(exampleViolationEventList)
    }

    @Test
    fun `calls getViolationEventsList from order information repository`() {
      service.getViolationEvents(legacySubjectId, false)

      Mockito.verify(orderEventsRepository, Mockito.times(1)).getViolationEventsList(legacySubjectId, false)
    }

    @Test
    fun `returns ViolationEventList when a response is received`() {
      var result = service.getViolationEvents(legacySubjectId, false)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the order when a response is received`() {
      var result = service.getViolationEvents(legacySubjectId, false)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(Event::class.java)
      Assertions.assertThat(result.first().legacyOrderId).isEqualTo(123)

      Assertions.assertThat(result.first().details).isInstanceOf(ViolationEventDetails::class.java)
      Assertions.assertThat(result.first().details.breachDetails).isEqualTo("TEST_BREACH")
    }
  }

  @Nested
  inner class GetContactEvents {
    val legacySubjectId = "fake-id"

    val exampleContactEventList = listOf<AthenaContactEventDTO>(
      AthenaContactEventDTO(
        legacySubjectId = 123,
        legacyOrderId = 123,
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
      Mockito.`when`(orderEventsRepository.getContactEventsList(legacySubjectId, false))
        .thenReturn(exampleContactEventList)
    }

    @Test
    fun `calls getContactEventsList from order information repository`() {
      service.getContactEvents(legacySubjectId, false)

      Mockito.verify(orderEventsRepository, Mockito.times(1)).getContactEventsList(legacySubjectId, false)
    }

    @Test
    fun `returns ContactsEventList when a response is received`() {
      var result = service.getContactEvents(legacySubjectId, false)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the order when a response is received`() {
      var result = service.getContactEvents(legacySubjectId, false)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(Event::class.java)
      Assertions.assertThat(result.first().legacyOrderId).isEqualTo(123)

      Assertions.assertThat(result.first().details).isInstanceOf(ContactEventDetails::class.java)
      Assertions.assertThat(result.first().details.type).isEqualTo("TEST_CONTACT")
    }
  }
}
