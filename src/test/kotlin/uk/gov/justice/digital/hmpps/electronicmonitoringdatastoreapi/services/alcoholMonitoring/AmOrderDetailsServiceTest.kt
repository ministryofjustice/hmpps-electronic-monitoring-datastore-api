package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring.AmOrderDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.alcoholMonitoring.AthenaAmOrderDetailsDTO
import java.time.LocalDateTime

class AmOrderDetailsServiceTest {
  private lateinit var amOrderDetailsRepository: AmOrderDetailsRepository
  private lateinit var service: AmOrderDetailsService

  @BeforeEach
  fun setup() {
    amOrderDetailsRepository = mock<AmOrderDetailsRepository>()
    service = AmOrderDetailsService(amOrderDetailsRepository)
  }

  val orderDetailsData = AthenaAmOrderDetailsDTO(
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
    responsibleOrganisationPhoneNumber = "01234567890",
    responsibleOrganisationEmail = "a@b.c",
    tagAtSource = "",
  )

  @Nested
  inner class GetOrderDetails {
    val orderId = "fake-id"

    @BeforeEach
    fun setup() {
      whenever(amOrderDetailsRepository.getOrderDetails(orderId))
        .thenReturn(orderDetailsData)
    }

    @Test
    fun `calls getAmOrderDetails from order details repository`() {
      service.getOrderDetails(orderId)
      verify(amOrderDetailsRepository, times(1)).getOrderDetails(orderId)
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
      whenever(amOrderDetailsRepository.searchOrders(orderSearchCriteria, false))
        .thenReturn("query-execution-id")

      service.getQueryExecutionId(orderSearchCriteria, false)

      verify(amOrderDetailsRepository, times(1)).searchOrders(orderSearchCriteria, false)
    }

    @Test
    fun `returns a query execution ID`() {
      val expectedResult = "query-execution-id"
      whenever(amOrderDetailsRepository.searchOrders(orderSearchCriteria, false))
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
      whenever(amOrderDetailsRepository.getSearchResults(queryExecutionId, false))
        .thenReturn(listOf())

      service.getSearchResults(queryExecutionId, false)

      verify(amOrderDetailsRepository, times(1)).getSearchResults(queryExecutionId, false)
    }

    @Test
    fun `returns empty list when no results are returned`() {
      whenever(amOrderDetailsRepository.getSearchResults(queryExecutionId, false))
        .thenReturn(listOf())

      val result = service.getSearchResults(queryExecutionId, false)

      Assertions.assertThat(result).isEmpty()
    }

    @Test
    fun `returns list of order search results when results are returned`() {
      whenever(amOrderDetailsRepository.getSearchResults(queryExecutionId, false))
        .thenReturn(
          listOf(
            orderDetailsData,
          ),
        )

      val result = service.getSearchResults(queryExecutionId, false)

      Assertions.assertThat(result.count()).isEqualTo(1)
    }
  }
}
