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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.DocumentList
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.KeyOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.SubjectHistoryReport
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.OrderController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@ActiveProfiles("test")
@JsonTest
class OrderControllerTest {
  private lateinit var orderService: OrderService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var controller: OrderController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = mock(Authentication::class.java)
    `when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    orderService = mock(OrderService::class.java)
    roleService = mock(AthenaRoleService::class.java)
    `when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.DEV)
    auditService = mock(AuditService::class.java)
    controller = OrderController(orderService, roleService, auditService)
  }

  @Nested
  inner class GetSpecialsOrder {
    @Test
    fun `gets specials order information from order service `() {
      val orderId = "1ab"
      val expectedResult = OrderInformation(
        keyOrderInformation = KeyOrderInformation(
          specials = "NO",
          legacySubjectId = "1234567",
          legacyOrderId = "7654321-DIFFERENT ID",
          name = "John Smith",
          alias = "Zeno",
          dateOfBirth = "01-02-1980",
          address1 = "1 Primary Street",
          address2 = "Sutton",
          address3 = "London",
          postcode = "ABC 123",
          orderStartDate = "01-02-2012",
          orderEndDate = "03-04-2013",
        ),
        subjectHistoryReport = SubjectHistoryReport(reportUrl = "#", name = "1234567", createdOn = "01-02-2020", time = "0900"),
        documents = DocumentList(
          pageSize = 0,
          orderDocuments = listOf(),
        ),
      )

      `when`(orderService.getOrderInformation(orderId, AthenaRole.DEV)).thenReturn(expectedResult)

      val result = controller.getSpecialsOrder(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isNotNull

      Mockito.verify(orderService, times(1)).getOrderInformation(orderId, AthenaRole.DEV)
    }
  }

  @Nested
  inner class GetOrderSummary {
    @Test
    fun `gets order information from order service`() {
      val orderId = "1ab"
      val expectedResult = OrderInformation(
        keyOrderInformation = KeyOrderInformation(
          specials = "NO",
          legacySubjectId = "1234567",
          legacyOrderId = "7654321-DIFFERENT ID",
          name = "John Smith",
          alias = "Zeno",
          dateOfBirth = "01-02-1980",
          address1 = "1 Primary Street",
          address2 = "Sutton",
          address3 = "London",
          postcode = "ABC 123",
          orderStartDate = "01-02-2012",
          orderEndDate = "03-04-2013",
        ),
        subjectHistoryReport = SubjectHistoryReport(reportUrl = "#", name = "1234567", createdOn = "01-02-2020", time = "0900"),
        documents = DocumentList(
          pageSize = 0,
          orderDocuments = listOf(),
        ),
      )

      `when`(orderService.getOrderInformation(orderId, AthenaRole.DEV)).thenReturn(expectedResult)

      val result = controller.getOrderSummary(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isNotNull

      Mockito.verify(orderService, times(1)).getOrderInformation(orderId, AthenaRole.DEV)
    }
  }
}
