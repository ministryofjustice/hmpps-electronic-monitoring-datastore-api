package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.ContactEventList
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.IncidentEventList
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.MonitoringEventList
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaIncidentEventListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaMonitoringEventListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchResultDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaStringQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSubjectHistoryReportDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderInformationRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService

class OrderServiceTest {
  private lateinit var orderRepository: OrderRepository
  private lateinit var orderInformationRepository: OrderInformationRepository
  private lateinit var service: OrderService

  @BeforeEach
  fun setup() {
    orderRepository = mock(OrderRepository::class.java)
    orderInformationRepository = mock(OrderInformationRepository::class.java)
    service = OrderService(orderRepository, orderInformationRepository)
  }

  @Test
  fun `OrderService can be instantiated`() {
    val sut = OrderService(mock(OrderRepository::class.java), mock(OrderInformationRepository::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class CheckAvailability {
    @Test
    fun `calls listLegacyIds from order repository`() {
      `when`(orderRepository.listLegacyIds(AthenaRole.DEV)).thenReturn(listOf<String>())

      service.checkAvailability(AthenaRole.DEV)

      Mockito.verify(orderRepository, times(1)).listLegacyIds(AthenaRole.DEV)
    }

    @Test
    fun `confirms AWS athena is available if successful`() {
      `when`(orderRepository.listLegacyIds(AthenaRole.DEV)).thenReturn(listOf<String>())

      var result = service.checkAvailability(AthenaRole.DEV)

      Assertions.assertThat(result).isTrue
    }

    @Test
    fun `confirms AWS athena is unavailable if not successful`() {
      val errorMessage = "fake error message"

      `when`(orderRepository.listLegacyIds(AthenaRole.DEV)).thenThrow(NullPointerException(errorMessage))

      var result = service.checkAvailability(AthenaRole.DEV)

      Assertions.assertThat(result).isFalse
    }
  }

  @Nested
  inner class Query {
    val athenaQuery = AthenaStringQuery("fake query")

    @Test
    fun `passes query to order repository`() {
      `when`(orderRepository.runQuery(athenaQuery, AthenaRole.DEV)).thenReturn("Expected response")

      service.query(athenaQuery, AthenaRole.DEV)

      Mockito.verify(orderRepository, times(1)).runQuery(athenaQuery, AthenaRole.DEV)
    }

    @Test
    fun `returns response from order repository`() {
      `when`(orderRepository.runQuery(athenaQuery, AthenaRole.DEV)).thenReturn("Expected response")

      var result = service.query(athenaQuery, AthenaRole.DEV)

      Assertions.assertThat(result).isEqualTo("Expected response")
    }
  }

  @Nested
  inner class Search {
    val orderSearchCriteria = OrderSearchCriteria(
      legacySubjectId = "fake-id",
    )

    @Test
    fun `calls searchOrders from order repository`() {
      `when`(orderRepository.searchOrders(orderSearchCriteria, AthenaRole.DEV))
        .thenReturn(listOf<AthenaOrderSearchResultDTO>())

      service.search(orderSearchCriteria, AthenaRole.DEV)

      Mockito.verify(orderRepository, times(1)).searchOrders(orderSearchCriteria, AthenaRole.DEV)
    }

    @Test
    fun `returns empty list when now results are returned`() {
      `when`(orderRepository.searchOrders(orderSearchCriteria, AthenaRole.DEV))
        .thenReturn(listOf<AthenaOrderSearchResultDTO>())

      var result = service.search(orderSearchCriteria, AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns list of order search results when results are returned`() {
      `when`(orderRepository.searchOrders(orderSearchCriteria, AthenaRole.DEV))
        .thenReturn(
          listOf<AthenaOrderSearchResultDTO>(
            AthenaOrderSearchResultDTO(
              legacySubjectId = 1,
              fullName = "",
              primaryAddressLine1 = "",
              primaryAddressLine2 = "",
              primaryAddressLine3 = "",
              primaryAddressPostCode = "",
              orderStartDate = "",
              orderEndDate = "",
            ),
          ),
        )

      var result = service.search(orderSearchCriteria, AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
      Assertions.assertThat(result.count()).isEqualTo(1)
      Assertions.assertThat(result.first()).isInstanceOf(OrderSearchResult::class.java)
    }
  }

  @Nested
  inner class GetOrderInformation {
    val orderId = "fake-id"

    val blankKeyOrderInformation = AthenaKeyOrderInformationDTO(
      legacySubjectId = "",
      legacyOrderId = orderId,
      name = "TEST",
      alias = "",
      dateOfBirth = "",
      address1 = "",
      address2 = "",
      address3 = "",
      postcode = "",
      orderStartDate = "",
      orderEndDate = "",
    )

    val blankSubjectHistoryReport = AthenaSubjectHistoryReportDTO(
      reportUrl = "",
      name = "TEST",
      createdOn = "",
      time = "",
    )

    val blankDocumentList = AthenaDocumentListDTO(
      pageSize = 200,
      orderDocuments = listOf(),
    )

    @BeforeEach
    fun setup() {
      `when`(orderInformationRepository.getKeyOrderInformation(orderId, AthenaRole.DEV))
        .thenReturn(blankKeyOrderInformation)
      `when`(orderInformationRepository.getSubjectHistoryReport(orderId, AthenaRole.DEV))
        .thenReturn(blankSubjectHistoryReport)
      `when`(orderInformationRepository.getDocumentList(orderId, AthenaRole.DEV))
        .thenReturn(blankDocumentList)
    }

    @Test
    fun `calls getKeyOrderInformation from order information repository`() {
      service.getOrderInformation(orderId, AthenaRole.DEV)

      Mockito.verify(orderInformationRepository, times(1)).getKeyOrderInformation(orderId, AthenaRole.DEV)
    }

    @Disabled("SubjectHistoryReport will no longer be used")
    @Test
    fun `calls getSubjectHistoryReport from order information repository`() {
      service.getOrderInformation(orderId, AthenaRole.DEV)

      Mockito.verify(orderInformationRepository, times(1)).getSubjectHistoryReport(orderId, AthenaRole.DEV)
    }

    @Disabled("We are not currently returning documents")
    @Test
    fun `calls getDocumentList from order information repository`() {
      service.getOrderInformation(orderId, AthenaRole.DEV)

      Mockito.verify(orderInformationRepository, times(1)).getDocumentList(orderId, AthenaRole.DEV)
    }

    @Test
    fun `returns OrderInformation when a document is found`() {
      var result = service.getOrderInformation(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(OrderInformation::class.java)
    }

    @Test
    fun `returns correct details of the order when a document is found`() {
      var result = service.getOrderInformation(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.keyOrderInformation.legacyOrderId).isEqualTo(orderId)
      Assertions.assertThat(result.keyOrderInformation.name).isEqualTo("TEST")
      Assertions.assertThat(result.subjectHistoryReport.name).isEqualTo("")
      Assertions.assertThat(result.documents.pageSize).isEqualTo(1)
      Assertions.assertThat(result.documents.orderDocuments).isEmpty()
    }
  }

  @Nested
  inner class GetMonitoringEventsList {
    val orderId = "fake-id"

    val exampleMonitoringEventList = AthenaMonitoringEventListDTO(
      pageSize = 200,
      events = listOf<AthenaMonitoringEventDTO>(
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
      `when`(orderInformationRepository.getMonitoringEventsList(orderId, AthenaRole.DEV))
        .thenReturn(exampleMonitoringEventList)
    }

    @Test
    fun `calls getMonitoringEventsList from order information repository`() {
      service.getMonitoringEvents(orderId, AthenaRole.DEV)

      Mockito.verify(orderInformationRepository, times(1)).getMonitoringEventsList(orderId, AthenaRole.DEV)
    }

    @Test
    fun `returns MonitoringEventList when a response is received`() {
      var result = service.getMonitoringEvents(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(MonitoringEventList::class.java)
    }

    @Test
    fun `returns correct details of the order when a response is received`() {
      var result = service.getMonitoringEvents(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.pageSize).isEqualTo(200)
      Assertions.assertThat(result.events.size).isEqualTo(1)
      Assertions.assertThat(result.events.first().legacyOrderId).isEqualTo(123)
      Assertions.assertThat(result.events.first().type).isEqualTo("TEST_EVENT")
    }
  }

  @Nested
  inner class GetIncidentEvents {
    val orderId = "fake-id"

    val exampleIncidentEventList = AthenaIncidentEventListDTO(
      pageSize = 200,
      events = listOf<AthenaIncidentEventDTO>(
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
      `when`(orderInformationRepository.getIncidentEventsList(orderId, AthenaRole.DEV))
        .thenReturn(exampleIncidentEventList)
    }

    @Test
    fun `calls getIncidentEventsList from order information repository`() {
      service.getIncidentEvents(orderId, AthenaRole.DEV)

      Mockito.verify(orderInformationRepository, times(1)).getIncidentEventsList(orderId, AthenaRole.DEV)
    }

    @Test
    fun `returns IncidentEventList when a response is received`() {
      var result = service.getIncidentEvents(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(IncidentEventList::class.java)
    }

    @Test
    fun `returns correct details of the order when a response is received`() {
      var result = service.getIncidentEvents(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.pageSize).isEqualTo(200)
      Assertions.assertThat(result.events.size).isEqualTo(1)
      Assertions.assertThat(result.events.first().legacyOrderId).isEqualTo(123)
      Assertions.assertThat(result.events.first().details.violation).isEqualTo("TEST_VIOLATION")
    }
  }

  @Nested
  inner class GetContactEvents {
    val orderId = "fake-id"

    val exampleContactEventList = AthenaContactEventListDTO(
      pageSize = 200,
      events = listOf<AthenaContactEventDTO>(
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
      `when`(orderInformationRepository.getContactEventsList(orderId, AthenaRole.DEV))
        .thenReturn(exampleContactEventList)
    }

    @Test
    fun `calls getContactEventsList from order information repository`() {
      service.getContactEvents(orderId, AthenaRole.DEV)

      Mockito.verify(orderInformationRepository, times(1)).getContactEventsList(orderId, AthenaRole.DEV)
    }

    @Test
    fun `returns ContactsEventList when a response is received`() {
      var result = service.getContactEvents(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(ContactEventList::class.java)
    }

    @Test
    fun `returns correct details of the order when a response is received`() {
      var result = service.getContactEvents(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.pageSize).isEqualTo(200)
      Assertions.assertThat(result.events.size).isEqualTo(1)
      Assertions.assertThat(result.events.first().legacyOrderId).isEqualTo(123)
      Assertions.assertThat(result.events.first().details.contactType).isEqualTo("TEST_CONTACT")
    }
  }
}
