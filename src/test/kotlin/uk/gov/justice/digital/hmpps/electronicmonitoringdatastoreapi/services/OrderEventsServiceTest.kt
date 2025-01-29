package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.ContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Event
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.IncidentEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.MonitoringEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaResultListDTO
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
    val orderId = "fake-id"

    val exampleMonitoringEventList = AthenaResultListDTO(
      pageSize = 200,
      items = listOf<AthenaMonitoringEventDTO>(
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
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(orderEventsRepository.getMonitoringEventsList(orderId, AthenaRole.DEV))
        .thenReturn(exampleMonitoringEventList)
    }

    @Test
    fun `calls getMonitoringEventsList from order information repository`() {
      service.getMonitoringEvents(orderId, AthenaRole.DEV)

      Mockito.verify(orderEventsRepository, Mockito.times(1)).getMonitoringEventsList(orderId, AthenaRole.DEV)
    }

    @Test
    fun `returns MonitoringEventList when a response is received`() {
      var result = service.getMonitoringEvents(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the order when a response is received`() {
      var result = service.getMonitoringEvents(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(Event::class.java)
      Assertions.assertThat(result.first().legacyOrderId).isEqualTo(123)

      Assertions.assertThat(result.first().details).isInstanceOf(MonitoringEventDetails::class.java)
      Assertions.assertThat(result.first().type).isEqualTo("TEST_EVENT")
    }
  }

  @Nested
  inner class GetIncidentEvents {
    val orderId = "fake-id"

    val exampleIncidentEventList = AthenaResultListDTO(
      pageSize = 200,
      items = listOf<AthenaIncidentEventDTO>(
        AthenaIncidentEventDTO(
          legacySubjectId = 123,
          legacyOrderId = 123,
          violationAlertType = "TEST_VIOLATION",
          violationAlertDate = "2019-06-24",
          violationAlertTime = "05:38:23",
        ),
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(orderEventsRepository.getIncidentEventsList(orderId, AthenaRole.DEV))
        .thenReturn(exampleIncidentEventList)
    }

    @Test
    fun `calls getIncidentEventsList from order information repository`() {
      service.getIncidentEvents(orderId, AthenaRole.DEV)

      Mockito.verify(orderEventsRepository, Mockito.times(1)).getIncidentEventsList(orderId, AthenaRole.DEV)
    }

    @Test
    fun `returns IncidentEventList when a response is received`() {
      var result = service.getIncidentEvents(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the order when a response is received`() {
      var result = service.getIncidentEvents(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(Event::class.java)
      Assertions.assertThat(result.first().legacyOrderId).isEqualTo(123)

      Assertions.assertThat(result.first().details).isInstanceOf(IncidentEventDetails::class.java)
      Assertions.assertThat(result.first().details.violation).isEqualTo("TEST_VIOLATION")
    }
  }

  @Nested
  inner class GetContactEvents {
    val orderId = "fake-id"

    val exampleContactEventList = AthenaResultListDTO(
      pageSize = 200,
      items = listOf<AthenaContactEventDTO>(
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
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(orderEventsRepository.getContactEventsList(orderId, AthenaRole.DEV))
        .thenReturn(exampleContactEventList)
    }

    @Test
    fun `calls getContactEventsList from order information repository`() {
      service.getContactEvents(orderId, AthenaRole.DEV)

      Mockito.verify(orderEventsRepository, Mockito.times(1)).getContactEventsList(orderId, AthenaRole.DEV)
    }

    @Test
    fun `returns ContactsEventList when a response is received`() {
      var result = service.getContactEvents(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the order when a response is received`() {
      var result = service.getContactEvents(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(Event::class.java)
      Assertions.assertThat(result.first().legacyOrderId).isEqualTo(123)

      Assertions.assertThat(result.first().details).isInstanceOf(ContactEventDetails::class.java)
      Assertions.assertThat(result.first().details.contactType).isEqualTo("TEST_CONTACT")
    }
  }
}
