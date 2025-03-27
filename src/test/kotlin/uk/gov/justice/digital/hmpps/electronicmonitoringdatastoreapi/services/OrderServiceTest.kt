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
      `when`(searchRepository.listLegacyIds(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(listOf<String>())

      service.checkAvailability(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(searchRepository, times(1)).listLegacyIds(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    @Test
    fun `confirms AWS athena is available if successful`() {
      `when`(searchRepository.listLegacyIds(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(listOf<String>())

      val result = service.checkAvailability(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isTrue
    }

    @Test
    fun `confirms AWS athena is unavailable if not successful`() {
      val errorMessage = "fake error message"

      `when`(searchRepository.listLegacyIds(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenThrow(NullPointerException(errorMessage))

      val result = service.checkAvailability(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isFalse
    }
  }

  @Nested
  inner class Query {
    val athenaQuery = AthenaStringQuery("fake query", arrayOf())

    @Test
    fun `passes query to order repository`() {
      `when`(searchRepository.runQuery(athenaQuery, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn("Expected response")

      service.query(athenaQuery, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(searchRepository, times(1)).runQuery(athenaQuery, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    @Test
    fun `returns response from order repository`() {
      `when`(searchRepository.runQuery(athenaQuery, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn("Expected response")

      val result = service.query(athenaQuery, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isEqualTo("Expected response")
    }
  }

  @Nested
  inner class SearchOrders {
    private val orderSearchCriteria = OrderSearchCriteria(
      legacySubjectId = "fake-id",
    )

    @Test
    fun `calls searchOrders from order repository`() {
      `when`(searchRepository.searchOrders(orderSearchCriteria, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
        .thenReturn("query-execution-id")

      service.getQueryExecutionId(orderSearchCriteria, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(searchRepository, times(1)).searchOrders(orderSearchCriteria, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    @Test
    fun `returns a query execution ID`() {
      val expectedResult = "query-execution-id"
      `when`(searchRepository.searchOrders(orderSearchCriteria, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
        .thenReturn("query-execution-id")

      val result = service.getQueryExecutionId(orderSearchCriteria, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isEqualTo(expectedResult)
    }
  }

  @Nested
  inner class GetSearchResults {
    private val queryExecutionId = "query-execution-id"

    @Test
    fun `calls searchOrders from order repository`() {
      `when`(searchRepository.getSearchResults(queryExecutionId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
        .thenReturn(listOf<AthenaOrderSearchResultDTO>())

      service.getSearchResults(queryExecutionId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(searchRepository, times(1)).getSearchResults(queryExecutionId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    @Test
    fun `returns empty list when no results are returned`() {
      `when`(searchRepository.getSearchResults(queryExecutionId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
        .thenReturn(listOf<AthenaOrderSearchResultDTO>())

      val result = service.getSearchResults(queryExecutionId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
      Assertions.assertThat(result.count()).isEqualTo(0)
    }

    @Test
    fun `returns list of order search results when results are returned`() {
      `when`(searchRepository.getSearchResults(queryExecutionId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
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

      val result = service.getSearchResults(queryExecutionId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

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
      `when`(orderInformationRepository.getKeyOrderInformation(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
        .thenReturn(blankKeyOrderInformation)
      `when`(orderInformationRepository.getSubjectHistoryReport(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
        .thenReturn(blankSubjectHistoryReport)
      `when`(orderInformationRepository.getDocumentList(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
        .thenReturn(blankDocumentList)
    }

    @Test
    fun `calls getKeyOrderInformation from order information repository`() {
      service.getOrderInformation(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(orderInformationRepository, times(1)).getKeyOrderInformation(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    @Disabled("SubjectHistoryReport will no longer be used")
    @Test
    fun `calls getSubjectHistoryReport from order information repository`() {
      service.getOrderInformation(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(orderInformationRepository, times(1)).getSubjectHistoryReport(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    @Disabled("We are not currently returning documents")
    @Test
    fun `calls getDocumentList from order information repository`() {
      service.getOrderInformation(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(orderInformationRepository, times(1)).getDocumentList(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    @Test
    fun `returns OrderInformation when a document is found`() {
      val result = service.getOrderInformation(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isInstanceOf(OrderInformation::class.java)
    }

    @Test
    fun `returns correct details of the order when a document is found`() {
      val result = service.getOrderInformation(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

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
      `when`(orderDetailsRepository.getOrderDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
        .thenReturn(blankOrderDetails)
    }

    @Test
    fun `calls getOrderDetails from order details repository`() {
      service.getOrderDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
      Mockito.verify(orderDetailsRepository, times(1)).getOrderDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    @Test
    fun `returns OrderDetails`() {
      val result = service.getOrderDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isInstanceOf(OrderDetails::class.java)
    }

    @Test
    fun `returns correct details of the order`() {
      val result = service.getOrderDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.legacySubjectId).isEqualTo("")
      Assertions.assertThat(result.specials).isEqualTo("no")
    }
  }
}
