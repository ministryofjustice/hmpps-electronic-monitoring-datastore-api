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
      `when`(searchRepository.listLegacyIds(false)).thenReturn(listOf<String>())

      service.checkAvailability()

      Mockito.verify(searchRepository, times(1)).listLegacyIds(false)
    }

    @Test
    fun `confirms AWS athena is available if successful`() {
      `when`(searchRepository.listLegacyIds(false)).thenReturn(listOf<String>())

      val result = service.checkAvailability()

      Assertions.assertThat(result).isTrue
    }

    @Test
    fun `confirms AWS athena is unavailable if not successful`() {
      val errorMessage = "fake error message"

      `when`(searchRepository.listLegacyIds(false)).thenThrow(NullPointerException(errorMessage))

      val result = service.checkAvailability()

      Assertions.assertThat(result).isFalse
    }
  }

  @Nested
  inner class Query {
    val athenaQuery = AthenaStringQuery("fake query", arrayOf())

    @Test
    fun `passes query to order repository`() {
      `when`(searchRepository.runQuery(athenaQuery, false)).thenReturn("Expected response")

      service.query(athenaQuery, false)

      Mockito.verify(searchRepository, times(1)).runQuery(athenaQuery, false)
    }

    @Test
    fun `returns response from order repository`() {
      `when`(searchRepository.runQuery(athenaQuery, false)).thenReturn("Expected response")

      val result = service.query(athenaQuery, false)

      Assertions.assertThat(result).isEqualTo("Expected response")
    }
  }

  @Nested
  inner class SearchOrders {
    private val orderSearchCriteria = OrderSearchCriteria(
      searchType = "integrity",
      legacySubjectId = "fake-id",
    )

    @Test
    fun `calls searchOrders from order repository`() {
      `when`(searchRepository.searchOrders(orderSearchCriteria, false))
        .thenReturn("query-execution-id")

      service.getQueryExecutionId(orderSearchCriteria, false)

      Mockito.verify(searchRepository, times(1)).searchOrders(orderSearchCriteria, false)
    }

    @Test
    fun `returns a query execution ID`() {
      val expectedResult = "query-execution-id"
      `when`(searchRepository.searchOrders(orderSearchCriteria, false))
        .thenReturn("query-execution-id")

      val result = service.getQueryExecutionId(orderSearchCriteria, false)

      Assertions.assertThat(result).isEqualTo(expectedResult)
    }
  }

  @Nested
  inner class GetSearchResults {
    private val queryExecutionId = "query-execution-id"

    @Test
    fun `calls searchOrders from order repository`() {
      `when`(searchRepository.getSearchResults(queryExecutionId, false))
        .thenReturn(listOf<AthenaOrderSearchResultDTO>())

      service.getSearchResults(queryExecutionId, false)

      Mockito.verify(searchRepository, times(1)).getSearchResults(queryExecutionId, false)
    }

    @Test
    fun `returns empty list when no results are returned`() {
      `when`(searchRepository.getSearchResults(queryExecutionId, false))
        .thenReturn(listOf<AthenaOrderSearchResultDTO>())

      val result = service.getSearchResults(queryExecutionId, false)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
      Assertions.assertThat(result.count()).isEqualTo(0)
    }

    @Test
    fun `returns list of order search results when results are returned`() {
      `when`(searchRepository.getSearchResults(queryExecutionId, false))
        .thenReturn(
          listOf<AthenaOrderSearchResultDTO>(
            AthenaOrderSearchResultDTO(
              legacySubjectId = 1,
              firstName = "",
              lastName = "",
              primaryAddressLine1 = "",
              primaryAddressLine2 = "",
              primaryAddressLine3 = "",
              primaryAddressPostCode = "",
              orderStartDate = "",
              orderEndDate = "",
            ),
          ),
        )

      val result = service.getSearchResults(queryExecutionId, false)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
      Assertions.assertThat(result.count()).isEqualTo(1)
      Assertions.assertThat(result.first()).isInstanceOf(OrderSearchResult::class.java)
    }
  }

  @Nested
  inner class GetOrderInformation {
    val legacySubjectId = "fake-id"

    val blankKeyOrderInformation = AthenaKeyOrderInformationDTO(
      legacySubjectId = "",
      legacyOrderId = legacySubjectId,
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
      `when`(orderInformationRepository.getKeyOrderInformation(legacySubjectId, false))
        .thenReturn(blankKeyOrderInformation)
      `when`(orderInformationRepository.getSubjectHistoryReport(legacySubjectId, false))
        .thenReturn(blankSubjectHistoryReport)
      `when`(orderInformationRepository.getDocumentList(legacySubjectId, false))
        .thenReturn(blankDocumentList)
    }

    @Test
    fun `calls getKeyOrderInformation from order information repository`() {
      service.getOrderInformation(legacySubjectId, false)

      Mockito.verify(orderInformationRepository, times(1)).getKeyOrderInformation(legacySubjectId, false)
    }

    @Disabled("SubjectHistoryReport will no longer be used")
    @Test
    fun `calls getSubjectHistoryReport from order information repository`() {
      service.getOrderInformation(legacySubjectId, false)

      Mockito.verify(orderInformationRepository, times(1)).getSubjectHistoryReport(legacySubjectId, false)
    }

    @Disabled("We are not currently returning documents")
    @Test
    fun `calls getDocumentList from order information repository`() {
      service.getOrderInformation(legacySubjectId, false)

      Mockito.verify(orderInformationRepository, times(1)).getDocumentList(legacySubjectId, false)
    }

    @Test
    fun `returns OrderInformation when a document is found`() {
      val result = service.getOrderInformation(legacySubjectId, false)

      Assertions.assertThat(result).isInstanceOf(OrderInformation::class.java)
    }

    @Test
    fun `returns correct details of the order when a document is found`() {
      val result = service.getOrderInformation(legacySubjectId, false)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.keyOrderInformation.legacyOrderId).isEqualTo(legacySubjectId)
      Assertions.assertThat(result.keyOrderInformation.name).isEqualTo("TEST")
      Assertions.assertThat(result.subjectHistoryReport.name).isEqualTo("")
      Assertions.assertThat(result.documents).isEmpty()
    }
  }

  @Nested
  inner class GetOrderDetails {
    val legacySubjectId = "fake-id"

    val blankOrderDetails = AthenaOrderDetailsDTO(
      legacySubjectId = "",
      legacyOrderId = "",
      offenceRisk = true,
    )

    @BeforeEach
    fun setup() {
      `when`(orderDetailsRepository.getOrderDetails(legacySubjectId, false))
        .thenReturn(blankOrderDetails)
    }

    @Test
    fun `calls getOrderDetails from order details repository`() {
      service.getOrderDetails(legacySubjectId, false)
      Mockito.verify(orderDetailsRepository, times(1)).getOrderDetails(legacySubjectId, false)
    }

    @Test
    fun `returns OrderDetails`() {
      val result = service.getOrderDetails(legacySubjectId, false)

      Assertions.assertThat(result).isInstanceOf(OrderDetails::class.java)
    }

    @Test
    fun `returns correct details of the order`() {
      val result = service.getOrderDetails(legacySubjectId, false)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.legacySubjectId).isEqualTo("")
      Assertions.assertThat(result.specials).isEqualTo("no")
    }
  }
}
