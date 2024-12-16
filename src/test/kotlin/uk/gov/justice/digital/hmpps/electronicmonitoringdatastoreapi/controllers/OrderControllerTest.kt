package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
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

      val result = sut.getSpecialsOrder(orderID, "fakeytoken")

      Assertions.assertThat(result.body).isEqualTo(expectedResult)
    }
  }

  @Nested
  inner class MockitoTest {

    @Nested
    inner class MockableObject {
      fun call(param: String): String = "You called me with '$param'"
    }

    @Test
    fun `Default behaviour works`() {
      val myObj = MockableObject()
      Assertions.assertThat(myObj.call("sheep")).isEqualTo("You called me with 'sheep'")
    }

    @Test
    fun `Fails when mock is not set up`() {
      val myObj: MockableObject = mock()
      Assertions.assertThat(myObj.call("sheep")).isNull()
    }

    @Test
    fun `Returns a different answer when mock is set up`() {
      val myObj: MockableObject = mock()
      val myParam: String = "sheep"
      `when`(myObj.call(myParam)).thenReturn("piglets")
      Assertions.assertThat(myObj.call(myParam)).isEqualTo("piglets")
    }

    @Test
    fun `Works with arbitrary inputs`() {
      val myObj: MockableObject = mock()
      val myParam: String = "sheep"
      `when`(myObj.call(anyString())).thenReturn("piglets")
      Assertions.assertThat(myObj.call(myParam)).isEqualTo("piglets")
    }
  }

  @Nested
  inner class GetOrderSummary {

    // TODO: Replace this with a test that mocks the response
    @Test
    fun `Returns order summary if correct params supplied`() {
      val orderId = "7654321"
      val userToken = "real-token"
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

      val result: ResponseEntity<OrderInformation> = sut.getOrderSummary(orderId, userToken)

      Assertions.assertThat(result.statusCode).isEqualTo(expectedResponse.statusCode)
      Assertions.assertThat(result.body).isEqualTo(expectedResponse.body)
      Mockito.verify(repository, times(1)).getMockOrderInformation(orderId)
      Mockito.verify(repository, times(1)).getKeyOrderInformation(orderId)
    }

    // TODO: Replace this with a test that mocks the response
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

    @Test
    fun `Throws AccessDeniedException if userToken is invalid`() {
      val orderId = "obviously-real-id"
      val userToken = "invalid-token"

      Assertions.assertThatExceptionOfType(AccessDeniedException::class.java)
        .isThrownBy { sut.getOrderSummary(orderId, userToken) }
        .withMessage("Your token is valid for this service, but your user is not allowed to access this resource")
    }
  }

  @Nested
  inner class CheckValidUser {

    @Test
    fun `Returns true if token is valid`() {
      val userToken = "real-token"
      val result = sut.checkValidUser(userToken)
      Assertions.assertThat(result).isTrue
    }

    @Test
    fun `Returns false if token is invalid`() {
      val userToken = "invalid-token"
      val result = sut.checkValidUser(userToken)
      Assertions.assertThat(result).isFalse
    }
  }
}
