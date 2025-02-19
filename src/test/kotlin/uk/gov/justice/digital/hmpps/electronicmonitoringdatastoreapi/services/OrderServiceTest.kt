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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaKeyOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchResultDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaStringQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSubjectHistoryReportDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderInformationRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.SearchRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService

class OrderServiceTest {
  private lateinit var searchRepository: SearchRepository
  private lateinit var orderInformationRepository: OrderInformationRepository
  private lateinit var orderDetailsRepository: OrderDetailsRepository
  private lateinit var service: OrderService

  @BeforeEach
  fun setup() {
    searchRepository = mock(SearchRepository::class.java)
    orderInformationRepository = mock(OrderInformationRepository::class.java)
    orderDetailsRepository = mock(OrderDetailsRepository::class.java)
    service = OrderService(searchRepository, orderInformationRepository, orderDetailsRepository)
  }

  @Test
  fun `OrderService can be instantiated`() {
    Assertions.assertThat(service).isNotNull()
  }

  @Nested
  inner class CheckAvailability {
    @Test
    fun `calls listLegacyIds from order repository`() {
      `when`(searchRepository.listLegacyIds(AthenaRole.DEV)).thenReturn(listOf<String>())

      service.checkAvailability(AthenaRole.DEV)

      Mockito.verify(searchRepository, times(1)).listLegacyIds(AthenaRole.DEV)
    }

    @Test
    fun `confirms AWS athena is available if successful`() {
      `when`(searchRepository.listLegacyIds(AthenaRole.DEV)).thenReturn(listOf<String>())

      var result = service.checkAvailability(AthenaRole.DEV)

      Assertions.assertThat(result).isTrue
    }

    @Test
    fun `confirms AWS athena is unavailable if not successful`() {
      val errorMessage = "fake error message"

      `when`(searchRepository.listLegacyIds(AthenaRole.DEV)).thenThrow(NullPointerException(errorMessage))

      var result = service.checkAvailability(AthenaRole.DEV)

      Assertions.assertThat(result).isFalse
    }
  }

  @Nested
  inner class Query {
    val athenaQuery = AthenaStringQuery("fake query")

    @Test
    fun `passes query to order repository`() {
      `when`(searchRepository.runQuery(athenaQuery, AthenaRole.DEV)).thenReturn("Expected response")

      service.query(athenaQuery, AthenaRole.DEV)

      Mockito.verify(searchRepository, times(1)).runQuery(athenaQuery, AthenaRole.DEV)
    }

    @Test
    fun `returns response from order repository`() {
      `when`(searchRepository.runQuery(athenaQuery, AthenaRole.DEV)).thenReturn("Expected response")

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
      `when`(searchRepository.searchOrders(orderSearchCriteria, AthenaRole.DEV))
        .thenReturn(listOf<AthenaOrderSearchResultDTO>())

      service.getQueryExecutionId(orderSearchCriteria, AthenaRole.DEV)

      Mockito.verify(searchRepository, times(1)).searchOrders(orderSearchCriteria, AthenaRole.DEV)
    }

    @Test
    fun `returns empty list when now results are returned`() {
      `when`(searchRepository.searchOrders(orderSearchCriteria, AthenaRole.DEV))
        .thenReturn(listOf<AthenaOrderSearchResultDTO>())

      var result = service.getQueryExecutionId(orderSearchCriteria, AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns list of order search results when results are returned`() {
      `when`(searchRepository.searchOrders(orderSearchCriteria, AthenaRole.DEV))
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

      var result = service.getQueryExecutionId(orderSearchCriteria, AthenaRole.DEV)

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

    val blankDocumentList = listOf<AthenaDocumentDTO>()

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
      Assertions.assertThat(result.documents).isEmpty()
    }
  }

  @Nested
  inner class GetOrderDetails {
    val orderId = "fake-id"

    val blankOrderDetails = AthenaOrderDetailsDTO(
      legacySubjectId = "",
      legacyOrderId = "",
      offenceRisk = true,
    )

    @BeforeEach
    fun setup() {
      `when`(orderDetailsRepository.getOrderDetails(orderId, AthenaRole.DEV))
        .thenReturn(blankOrderDetails)
    }

    @Test
    fun `calls getOrderDetails from order details repository`() {
      service.getOrderDetails(orderId, AthenaRole.DEV)
      Mockito.verify(orderDetailsRepository, times(1)).getOrderDetails(orderId, AthenaRole.DEV)
    }

    @Test
    fun `returns OrderDetails`() {
      val result = service.getOrderDetails(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(OrderDetails::class.java)
    }

    @Test
    fun `returns correct details of the order`() {
      val result = service.getOrderDetails(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.legacySubjectId).isEqualTo("")
      Assertions.assertThat(result.specials).isEqualTo("no")
    }
  }
}
