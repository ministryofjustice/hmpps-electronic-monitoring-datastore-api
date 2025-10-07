package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.AmOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.AlcoholMonitoringOrderDetailsRepository
import java.time.LocalDateTime

class AlcoholMonitoringOrderDetailsServiceTest {
  private lateinit var alcoholMonitoringOrderDetailsRepository: AlcoholMonitoringOrderDetailsRepository
  private lateinit var service: AlcoholMonitoringOrderDetailsService

  @BeforeEach
  fun setup() {
    alcoholMonitoringOrderDetailsRepository = mock<AlcoholMonitoringOrderDetailsRepository>()
    service = AlcoholMonitoringOrderDetailsService(alcoholMonitoringOrderDetailsRepository)
  }

  val orderDetailsData = AmOrderDetails(
    legacySubjectId = "AA2020",
    firstName = "John",
    lastName = "Smith",
    alias = "Zeno",
    dateOfBirth = "1980-02-01",
    legacyGender = "Sex",
    specialInstructions = "Special instructions",
    phoneOrMobileNumber = "09876543210",
    primaryAddressLine1 = "1 Primary Street",
    primaryAddressLine2 = "Sutton",
    primaryAddressLine3 = "London",
    primaryAddressPostCode = "ABC 123",
    legacyOrderId = "1234567",
    orderStartDate = "2012-02-01",
    orderEndDate = "2013-04-03",
    enforceableCondition = "Enforceable condition",
    orderType = "Community",
    orderTypeDescription = "",
    orderEndOutcome = "",
    responsibleOrgDetailsPhoneNumber = "01234567890",
    responsibleOrgDetailsEmail = "a@b.c",
    tagAtSource = "",
  )

  @Nested
  inner class GetOrderDetails {
    val orderId = "fake-id"

    @BeforeEach
    fun setup() {
      whenever(alcoholMonitoringOrderDetailsRepository.getByLegacySubjectId(orderId))
        .thenReturn(orderDetailsData)
    }

    @Test
    fun `calls getAlcoholMonitoringOrderDetails from order details repository`() {
      service.getOrderDetails(orderId)
      verify(alcoholMonitoringOrderDetailsRepository, times(1)).getByLegacySubjectId(orderId)
    }

    @Test
    fun `returns correct details of the order`() {
      val result = service.getOrderDetails(orderId)

      Assertions.assertThat(result.legacySubjectId).isEqualTo("AA2020")
      Assertions.assertThat(result.firstName).isEqualTo("John")
      Assertions.assertThat(result.orderEndDate).isEqualTo(LocalDateTime.parse("2013-04-03T00:00:00"))
    }
  }

  @Nested
  inner class SearchOrders {
    private val orderSearchCriteria = OrderSearchCriteria(
      legacySubjectId = "fake-id",
    )

    @Test
    fun `calls searchOrders from order repository`() {
      whenever(alcoholMonitoringOrderDetailsRepository.searchOrders(orderSearchCriteria))
        .thenReturn("query-execution-id")

      service.getQueryExecutionId(orderSearchCriteria)

      verify(alcoholMonitoringOrderDetailsRepository, times(1)).searchOrders(orderSearchCriteria)
    }

    @Test
    fun `returns a query execution ID`() {
      val expectedResult = "query-execution-id"
      whenever(alcoholMonitoringOrderDetailsRepository.searchOrders(orderSearchCriteria))
        .thenReturn("query-execution-id")

      val result = service.getQueryExecutionId(orderSearchCriteria)

      Assertions.assertThat(result).isEqualTo(expectedResult)
    }
  }

  @Nested
  inner class GetSearchResults {
    private val queryExecutionId = "query-execution-id"

    @Test
    fun `calls searchOrders from order repository`() {
      whenever(alcoholMonitoringOrderDetailsRepository.getSearchResults(queryExecutionId))
        .thenReturn(listOf())

      service.getSearchResults(queryExecutionId)

      verify(alcoholMonitoringOrderDetailsRepository, times(1)).getSearchResults(queryExecutionId)
    }

    @Test
    fun `returns empty list when no results are returned`() {
      whenever(alcoholMonitoringOrderDetailsRepository.getSearchResults(queryExecutionId))
        .thenReturn(listOf())

      val result = service.getSearchResults(queryExecutionId)

      Assertions.assertThat(result).isEmpty()
    }

    @Test
    fun `returns list of order search results when results are returned`() {
      whenever(alcoholMonitoringOrderDetailsRepository.getSearchResults(queryExecutionId))
        .thenReturn(
          listOf(
            orderDetailsData,
          ),
        )

      val result = service.getSearchResults(queryExecutionId)

      Assertions.assertThat(result.count()).isEqualTo(1)
    }
  }
}
