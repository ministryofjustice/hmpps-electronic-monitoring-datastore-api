package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchResultDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.SearchRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderSearchService

class OrderServiceTest {
  private lateinit var searchRepository: SearchRepository
  private lateinit var service: OrderSearchService

  @BeforeEach
  fun setup() {
    searchRepository = mock(SearchRepository::class.java)
    service = OrderSearchService(searchRepository)
  }

  @Test
  fun `OrderService can be instantiated`() {
    Assertions.assertThat(service).isNotNull()
  }

  @Nested
  inner class CheckAvailability {
    @Test
    fun `calls listLegacyIds from order repository`() {
      `when`(searchRepository.listLegacyIds(false)).thenReturn(listOf("fake-id"))

      service.checkAvailability(false)

      Mockito.verify(searchRepository, times(1)).listLegacyIds(false)
    }

    @Test
    fun `confirms AWS athena is available if successful`() {
      `when`(searchRepository.listLegacyIds(false)).thenReturn(listOf("fake-id"))

      val result = service.checkAvailability(false)

      Assertions.assertThat(result).isTrue
    }

    @Test
    fun `confirms AWS athena is unavailable if not successful by not handling error`() {
      val errorMessage = "fake error message"

      `when`(searchRepository.listLegacyIds(false)).thenThrow(NullPointerException(errorMessage))

      Assertions.assertThatThrownBy { service.checkAvailability(false) }.isInstanceOf(RuntimeException::class.java)
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
        .thenReturn(listOf())

      service.getSearchResults(queryExecutionId, false)

      Mockito.verify(searchRepository, times(1)).getSearchResults(queryExecutionId, false)
    }

    @Test
    fun `returns empty list when no results are returned`() {
      `when`(searchRepository.getSearchResults(queryExecutionId, false))
        .thenReturn(listOf())

      val result = service.getSearchResults(queryExecutionId, false)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
      Assertions.assertThat(result.count()).isEqualTo(0)
    }

    @Test
    fun `returns list of order search results when results are returned`() {
      `when`(searchRepository.getSearchResults(queryExecutionId, false))
        .thenReturn(
          listOf(
            AthenaOrderSearchResultDTO(
              legacySubjectId = "1",
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
}
