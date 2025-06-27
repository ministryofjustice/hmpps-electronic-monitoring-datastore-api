package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources.integrity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityDocument
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityKeyOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegritySubjectHistoryReport
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.integrity.IntegrityOrderInformationController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.IntegrityOrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class IntegritySummaryControllerTest {
  private lateinit var integrityOrderService: IntegrityOrderService
  private lateinit var auditService: AuditService
  private lateinit var controller: IntegrityOrderInformationController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    integrityOrderService = Mockito.mock(IntegrityOrderService::class.java)
    auditService = Mockito.mock(AuditService::class.java)
    controller = IntegrityOrderInformationController(integrityOrderService, auditService)
  }

  @Nested
  inner class GetOrderSummary {
    @Test
    fun `gets summary from order service`() {
      val legacySubjectId = "1ab"
      val expectedResult = IntegrityOrderInformation(
        keyOrderInformation = IntegrityKeyOrderInformation(
          specials = "NO",
          legacySubjectId = "1234567",
          legacyOrderId = "7654321-DIFFERENT ID",
          name = "John Smith",
          alias = "Zeno",
          dateOfBirth = LocalDateTime.parse("1980-02-01T00:00:00"),
          address1 = "1 Primary Street",
          address2 = "Sutton",
          address3 = "London",
          postcode = "ABC 123",
          orderStartDate = LocalDateTime.parse("2012-02-01T00:00:00"),
          orderEndDate = LocalDateTime.parse("2013-04-03T00:00:00"),
        ),
        subjectHistoryReport = IntegritySubjectHistoryReport(
          reportUrl = "#",
          name = "1234567",
          createdOn = "01-02-2020",
          time = "0900",
        ),
        documents = listOf<IntegrityDocument>(),
      )

      Mockito.`when`(integrityOrderService.getOrderInformation(legacySubjectId, false)).thenReturn(expectedResult)

      val result = controller.getSummary(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isNotNull
      Assertions.assertThat(result.body).isInstanceOf(IntegrityOrderInformation::class.java)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)
      Mockito.verify(integrityOrderService, Mockito.times(1)).getOrderInformation(legacySubjectId, false)
    }
  }
}
