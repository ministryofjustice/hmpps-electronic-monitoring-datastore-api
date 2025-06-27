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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchResultDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaStringQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityDocumentDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityKeyOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityOrderDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegritySubjectHistoryReportDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.SearchRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityOrderDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityOrderInformationRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.IntegrityOrderService

class OrderServiceTest {
  private lateinit var searchRepository: SearchRepository
  private lateinit var integrityOrderInformationRepository: IntegrityOrderInformationRepository
  private lateinit var integrityOrderDetailsRepository: IntegrityOrderDetailsRepository
  private lateinit var service: IntegrityOrderService

  @BeforeEach
  fun setup() {
    searchRepository = mock(SearchRepository::class.java)
    integrityOrderInformationRepository = mock(IntegrityOrderInformationRepository::class.java)
    integrityOrderDetailsRepository = mock(IntegrityOrderDetailsRepository::class.java)
    service = IntegrityOrderService(searchRepository, integrityOrderInformationRepository, integrityOrderDetailsRepository)
  }

  @Test
  fun `OrderService can be instantiated`() {
    Assertions.assertThat(service).isNotNull()
  }

  @Nested
  inner class CheckAvailability {
    @Test
    fun `calls listLegacyIds from order repository`() {
      `when`(searchRepository.listLegacyIds(AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)).thenReturn(listOf())

      service.checkAvailability(AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Mockito.verify(searchRepository, times(1)).listLegacyIds(AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)
    }

    @Test
    fun `confirms AWS athena is available if successful`() {
      `when`(searchRepository.listLegacyIds(AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)).thenReturn(listOf())

      val result = service.checkAvailability(AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Assertions.assertThat(result).isTrue
    }

    @Test
    fun `confirms AWS athena is unavailable if not successful`() {
      val errorMessage = "fake error message"

      `when`(searchRepository.listLegacyIds(AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)).thenThrow(NullPointerException(errorMessage))

      val result = service.checkAvailability(AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Assertions.assertThat(result).isFalse
    }
  }

  @Nested
  inner class Query {
    val athenaQuery = AthenaStringQuery("fake query", arrayOf())

    @Test
    fun `passes query to order repository`() {
      `when`(searchRepository.runQuery(athenaQuery, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)).thenReturn("Expected response")

      service.query(athenaQuery, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Mockito.verify(searchRepository, times(1)).runQuery(athenaQuery, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)
    }

    @Test
    fun `returns response from order repository`() {
      `when`(searchRepository.runQuery(athenaQuery, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)).thenReturn("Expected response")

      val result = service.query(athenaQuery, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

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
      `when`(searchRepository.getSearchResults(queryExecutionId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO))
        .thenReturn(listOf())

      service.getSearchResults(queryExecutionId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Mockito.verify(searchRepository, times(1)).getSearchResults(queryExecutionId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)
    }

    @Test
    fun `returns empty list when no results are returned`() {
      `when`(searchRepository.getSearchResults(queryExecutionId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO))
        .thenReturn(listOf())

      val result = service.getSearchResults(queryExecutionId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
      Assertions.assertThat(result.count()).isEqualTo(0)
    }

    @Test
    fun `returns list of order search results when results are returned`() {
      `when`(searchRepository.getSearchResults(queryExecutionId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO))
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

      val result = service.getSearchResults(queryExecutionId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
      Assertions.assertThat(result.count()).isEqualTo(1)
      Assertions.assertThat(result.first()).isInstanceOf(OrderSearchResult::class.java)
    }
  }

  @Nested
  inner class GetOrderInformation {
    val legacySubjectId = "fake-id"

    val blankKeyOrderInformation = AthenaIntegrityKeyOrderInformationDTO(
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

    val blankSubjectHistoryReport = AthenaIntegritySubjectHistoryReportDTO(
      reportUrl = "",
      name = "TEST",
      createdOn = "",
      time = "",
    )

    val blankDocumentList = listOf<AthenaIntegrityDocumentDTO>()

    @BeforeEach
    fun setup() {
      `when`(integrityOrderInformationRepository.getKeyOrderInformation(legacySubjectId, false))
        .thenReturn(blankKeyOrderInformation)
      `when`(integrityOrderInformationRepository.getSubjectHistoryReport(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO))
        .thenReturn(blankSubjectHistoryReport)
      `when`(integrityOrderInformationRepository.getDocumentList(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO))
        .thenReturn(blankDocumentList)
    }

    @Test
    fun `calls getKeyOrderInformation from order information repository`() {
      service.getOrderInformation(legacySubjectId, false)

      Mockito.verify(integrityOrderInformationRepository, times(1)).getKeyOrderInformation(legacySubjectId, false)
    }

    @Disabled("SubjectHistoryReport will no longer be used")
    @Test
    fun `calls getSubjectHistoryReport from order information repository`() {
      service.getOrderInformation(legacySubjectId, false)

      Mockito.verify(integrityOrderInformationRepository, times(1)).getSubjectHistoryReport(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)
    }

    @Disabled("We are not currently returning documents")
    @Test
    fun `calls getDocumentList from order information repository`() {
      service.getOrderInformation(legacySubjectId, false)

      Mockito.verify(integrityOrderInformationRepository, times(1)).getDocumentList(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)
    }

    @Test
    fun `returns OrderInformation when a document is found`() {
      val result = service.getOrderInformation(legacySubjectId, false)

      Assertions.assertThat(result).isInstanceOf(IntegrityOrderInformation::class.java)
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

    val blankOrderDetails = AthenaIntegrityOrderDetailsDTO(
      legacySubjectId = "",
      legacyOrderId = "",
      offenceRisk = true,
    )

    @BeforeEach
    fun setup() {
      `when`(integrityOrderDetailsRepository.getOrderDetails(legacySubjectId, false))
        .thenReturn(blankOrderDetails)
    }

    @Test
    fun `calls getOrderDetails from order details repository`() {
      service.getOrderDetails(legacySubjectId, false)
      Mockito.verify(integrityOrderDetailsRepository, times(1)).getOrderDetails(legacySubjectId, false)
    }

    @Test
    fun `returns OrderDetails`() {
      val result = service.getOrderDetails(legacySubjectId, false)

      Assertions.assertThat(result).isInstanceOf(IntegrityOrderDetails::class.java)
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
