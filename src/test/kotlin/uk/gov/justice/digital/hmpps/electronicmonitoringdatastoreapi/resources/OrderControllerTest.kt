package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AmOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.CapOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Document
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderDetailsBase
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.SubjectHistoryReport
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.OrderController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AmOrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@ActiveProfiles("test")
@JsonTest
class OrderControllerTest {
  private lateinit var orderService: OrderService
  private lateinit var amOrderService: AmOrderService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var controller: OrderController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = mock(Authentication::class.java)
    `when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    orderService = mock(OrderService::class.java)
    amOrderService = mock(AmOrderService::class.java)
    roleService = mock(AthenaRoleService::class.java)
    `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.DEV)
    auditService = mock(AuditService::class.java)
    controller = OrderController(orderService, amOrderService, roleService, auditService)
  }

  @Nested
  inner class GetSpecialsOrder {
    @Test
    fun `gets specials order information from order service `() {
      val orderId = "1ab"
      val expectedResult = OrderInformation(
        keyOrderInformation = OrderDetailsBase(
          type = "BASE",
          specials = "NO",
          legacySubjectId = "1234567",
          legacyOrderId = "7654321-DIFFERENT ID",
          firstName = "John",
          lastName = "Smith",
          alias = "Zeno",
          dateOfBirth = "01-02-1980",
          primaryAddressLine1 = "1 Primary Street",
          primaryAddressLine2 = "Sutton",
          primaryAddressLine3 = "London",
          primaryAddressPostCode = "ABC 123",
          orderStartDate = "01-02-2012",
          orderEndDate = "03-04-2013",
        ),
        subjectHistoryReport = SubjectHistoryReport(
          reportUrl = "#",
          name = "1234567",
          createdOn = "01-02-2020",
          time = "0900",
        ),
        documents = listOf<Document>(),
      )

      `when`(orderService.getOrderInformation(orderId, AthenaRole.DEV)).thenReturn(expectedResult)

      val result = controller.getSpecialsOrder(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(orderService, times(1)).getOrderInformation(orderId, AthenaRole.DEV)
    }
  }

  @Nested
  inner class GetOrderSummary {
    @Test
    fun `gets order information from order service`() {
      val orderId = "1ab"
      val expectedResult = OrderInformation(
        keyOrderInformation = OrderDetailsBase(
          type = "BASE",
          specials = "NO",
          legacySubjectId = "1234567",
          legacyOrderId = "7654321-DIFFERENT ID",
          firstName = "John",
          lastName = "Smith",
          alias = "Zeno",
          dateOfBirth = "01-02-1980",
          primaryAddressLine1 = "1 Primary Street",
          primaryAddressLine2 = "Sutton",
          primaryAddressLine3 = "London",
          primaryAddressPostCode = "ABC 123",
          orderStartDate = "01-02-2012",
          orderEndDate = "03-04-2013",
        ),
        subjectHistoryReport = SubjectHistoryReport(
          reportUrl = "#",
          name = "1234567",
          createdOn = "01-02-2020",
          time = "0900",
        ),
        documents = listOf<Document>(),
      )

      `when`(orderService.getOrderInformation(orderId, AthenaRole.DEV)).thenReturn(expectedResult)

      val result = controller.getOrderSummary(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isNotNull
      Assertions.assertThat(result.body).isInstanceOf(OrderInformation::class.java)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)
      Mockito.verify(orderService, times(1)).getOrderInformation(orderId, AthenaRole.DEV)
    }
  }

  @Nested
  inner class GetCapOrderDetails {
    @Test
    fun `Returns the appropriate object type`() {
      val orderId = "1ab"
      val expectedResult: CapOrderDetails = CapOrderDetails(
        specials = "no",
        legacySubjectId = "",
        legacyOrderId = "",
        offenceRisk = false,
      )

      `when`(orderService.getOrderDetails(orderId, AthenaRole.DEV)).thenReturn(expectedResult)

      val result = controller.getOrderDetails(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isNotNull
      Assertions.assertThat(result.body).isInstanceOf(CapOrderDetails::class.java)
    }

    @Test
    fun `gets order details from order service`() {
      val orderId = "1ab"
      val expectedResult: CapOrderDetails = CapOrderDetails(
        specials = "no",
        legacySubjectId = "",
        legacyOrderId = "",
        offenceRisk = false,
      )

      `when`(orderService.getOrderDetails(orderId, AthenaRole.DEV)).thenReturn(expectedResult)

      val result = controller.getOrderDetails(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(orderService, times(1)).getOrderDetails(orderId, AthenaRole.DEV)
    }
  }

  @Nested
  inner class GetAmCapOrderDetails {
    @Test
    fun `Returns the appropriate object type`() {
      val orderId = "1ab"
      val expectedResult: AmOrderDetails = AmOrderDetails(
        specials = "no",
        legacySubjectId = "",
        legacyOrderId = "",
        responsibleOrgDetailsPhoneNumber = "07777777777",
      )

      `when`(amOrderService.getAmOrderDetails(orderId, AthenaRole.DEV)).thenReturn(expectedResult)

      val result = controller.getAmOrderDetails(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isNotNull
      Assertions.assertThat(result.body).isInstanceOf(AmOrderDetails::class.java)
    }

    @Test
    fun `gets order details from order service`() {
      val orderId = "1ab"
      val expectedResult: AmOrderDetails = AmOrderDetails(
        specials = "no",
        legacySubjectId = "",
        legacyOrderId = "",
        responsibleOrgDetailsPhoneNumber = "07777777777",
      )

      `when`(amOrderService.getAmOrderDetails(orderId, AthenaRole.DEV)).thenReturn(expectedResult)

      val result = controller.getAmOrderDetails(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(amOrderService, times(1)).getAmOrderDetails(orderId, AthenaRole.DEV)
    }
  }
}
