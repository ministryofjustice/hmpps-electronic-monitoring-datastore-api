package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.integrity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityOrderDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.models.integrity.AthenaIntegrityOrderDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity.IntegrityOrderDetailsService
import java.time.LocalDateTime

class IntegrityOrderDetailsServiceTest {
  private lateinit var integrityOrderDetailsRepository: IntegrityOrderDetailsRepository
  private lateinit var service: IntegrityOrderDetailsService

  @BeforeEach
  fun setup() {
    integrityOrderDetailsRepository = mock<IntegrityOrderDetailsRepository>()
    service = IntegrityOrderDetailsService(integrityOrderDetailsRepository)
  }

  val orderDetailsData = AthenaIntegrityOrderDetailsDTO(
    legacySubjectId = "AA2020",
    firstName = "John",
    lastName = "Smith",
    alias = "Zeno",
    dateOfBirth = "1980-02-01",
    sex = "Sex",
    phoneOrMobileNumber = "09876543210",
    primaryAddressLine1 = "1 Primary Street",
    primaryAddressLine2 = "Sutton",
    primaryAddressLine3 = "London",
    primaryAddressPostCode = "ABC 123",
    legacyOrderId = "1234567",
    orderStartDate = "2012-02-01",
    orderEndDate = "2013-04-03",
    orderType = "Community",
    orderTypeDescription = "",
    adultOrChild = "adult",
    contact = "",
    ppo = "",
    mappa = "",
    technicalBail = "",
    manualRisk = "",
    offenceRisk = true,
    postCodeRisk = "",
    falseLimbRisk = "",
    migratedRisk = "",
    rangeRisk = "",
    reportRisk = "",
    orderTypeDetail = "",
    wearingWristPid = "",
    notifyingOrganisationDetailsName = "",
    responsibleOrganisation = "",
    responsibleOrganisationDetailsRegion = "",
  )

  @Nested
  inner class GetOrderDetails {
    val legacySubjectId = "fake-id"

    @BeforeEach
    fun setup() {
      whenever(integrityOrderDetailsRepository.getOrderDetails(legacySubjectId, false))
        .thenReturn(orderDetailsData)
    }

    @Test
    fun `calls getOrderDetails from integrity order details repository`() {
      service.getOrderDetails(legacySubjectId, false)
      verify(integrityOrderDetailsRepository, times(1)).getOrderDetails(legacySubjectId, false)
    }

    @Test
    fun `returns correct details of the order`() {
      val result = service.getOrderDetails(legacySubjectId, false)

      Assertions.assertThat(result.legacySubjectId).isEqualTo("AA2020")
      Assertions.assertThat(result.firstName).isEqualTo("John")
      Assertions.assertThat(result.specials).isEqualTo("no")
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
      whenever(integrityOrderDetailsRepository.searchOrders(orderSearchCriteria, false))
        .thenReturn("query-execution-id")

      service.getQueryExecutionId(orderSearchCriteria, false)

      verify(integrityOrderDetailsRepository, times(1)).searchOrders(orderSearchCriteria, false)
    }

    @Test
    fun `returns a query execution ID`() {
      val expectedResult = "query-execution-id"
      whenever(integrityOrderDetailsRepository.searchOrders(orderSearchCriteria, false))
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
      whenever(integrityOrderDetailsRepository.getSearchResults(queryExecutionId, false))
        .thenReturn(listOf())

      service.getSearchResults(queryExecutionId, false)

      verify(integrityOrderDetailsRepository, times(1)).getSearchResults(queryExecutionId, false)
    }

    @Test
    fun `returns empty list when no results are returned`() {
      whenever(integrityOrderDetailsRepository.getSearchResults(queryExecutionId, false))
        .thenReturn(listOf())

      val result = service.getSearchResults(queryExecutionId, false)

      Assertions.assertThat(result).isEmpty()
    }

    @Test
    fun `returns list of order search results when results are returned`() {
      whenever(integrityOrderDetailsRepository.getSearchResults(queryExecutionId, false))
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
