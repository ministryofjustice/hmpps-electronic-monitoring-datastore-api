package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AthenaQueryResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.KeyOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderInformationRepository

@ActiveProfiles("test")
class OrderControllerTest {

  private val objectMapper = ObjectMapper()
  private lateinit var repository: OrderInformationRepository
  private lateinit var sut: OrderController

  @BeforeEach
  fun setup() {
    repository = mock()
    sut = OrderController(repository)
  }

  @Nested
  inner class GetSpecialsOrder {
    @Test
    fun `is callable with required parameters and correctly uses mocked service`() {
      val orderID: String = "1ab"
      val expectedResult: OrderInformation = OrderInformationRepository().getMockOrderInformation("DIFFERENT ID")
      `when`(repository.getMockOrderInformation(orderID)).thenReturn(expectedResult)

      val result = sut.getSpecialsOrder(orderID, "fake-auth-header")

      Assertions.assertThat(result.body).isEqualTo(expectedResult)
    }
  }

  @Nested
  inner class GetOrderSummary {

    @Test
    fun `Returns order summary if correct params supplied`() {
      val orderId = "7654321"
      val authorizationHeader = "real-token"
      val fakeOrder = OrderInformationRepository().getMockOrderInformation("this is fake info")

      val expectedServiceResult = OrderInformationRepository()
        .getMockOrderInformation("this is the real info")
        .keyOrderInformation

      val expectedResponse = ResponseEntity.ok(
        OrderInformation(
          keyOrderInformation = expectedServiceResult,
          subjectHistoryReport = fakeOrder.subjectHistoryReport,
          documents = fakeOrder.documents,
        ),
      )

      `when`(repository.getMockOrderInformation(orderId))
        .thenReturn(fakeOrder)
      `when`(repository.getKeyOrderInformation(orderId))
        .thenReturn(
          AthenaQueryResponse<KeyOrderInformation>(
            queryString = "fake string",
            athenaRole = "fake role",
            queryResponse = expectedServiceResult,
          ),
        )

      val result: ResponseEntity<OrderInformation> = sut.getOrderSummary(orderId, authorizationHeader)

      Assertions.assertThat(result.statusCode).isEqualTo(expectedResponse.statusCode)
      Assertions.assertThat(result.body).isEqualTo(expectedResponse.body)
      Mockito.verify(repository, times(1)).getMockOrderInformation(orderId)
      Mockito.verify(repository, times(1)).getKeyOrderInformation(orderId)
    }

    // TODO: Replace this with a test that mocks the response
    // TODO: For this to work, need to implement sensible responses from the service layer if order not found
//    @Test
//    fun `Returns 'No summary available' if incorrect orderId`() {
//      val orderId = "non-existent-order"
//      val userToken = "real-token"
//      val expectedResponse = ResponseEntity.notFound().build<Any>()
//
//      val result: ResponseEntity<OrderInformation> = sut.getMockOrderSummary(orderId, userToken)
//      Assertions.assertThat(result.statusCode).isEqualTo(expectedResponse.statusCode)
//      // Checking for body on notFound as it should not contain a body
//      Assertions.assertThat(result.body).isNull()
//    }
  }
}
