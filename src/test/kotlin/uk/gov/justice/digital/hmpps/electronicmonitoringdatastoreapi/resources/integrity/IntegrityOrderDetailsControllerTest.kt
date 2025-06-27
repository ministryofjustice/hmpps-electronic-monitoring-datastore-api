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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.integrity.IntegrityOrderDetailsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.IntegrityOrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class IntegrityOrderDetailsControllerTest {
  private lateinit var integrityOrderDetailsService: IntegrityOrderService
  private lateinit var auditService: AuditService
  private lateinit var controller: IntegrityOrderDetailsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    integrityOrderDetailsService = Mockito.mock(IntegrityOrderService::class.java)
    auditService = Mockito.mock(AuditService::class.java)
    controller = IntegrityOrderDetailsController(integrityOrderDetailsService, auditService)
  }

  @Nested
  inner class GetIntegrityGeneralOrderDetails {
    @Test
    fun `gets general order details from order details service`() {
      val legacySubjectId = "2gt"
      val expectedResult = IntegrityOrderDetails(
        specials = "no",
        legacySubjectId = "AA12345",
        legacyOrderId = "1234567",
        firstName = "John",
        lastName = "Smith",
        alias = "Zeno",
        dateOfBirth = LocalDateTime.parse("1980-02-01T00:00:00"),
        adultOrChild = "adult",
        sex = "Sex",
        contact = "contact",
        primaryAddressLine1 = "1 Primary Street",
        primaryAddressLine2 = "Sutton",
        primaryAddressLine3 = "London",
        primaryAddressPostCode = "ABC 123",
        phoneOrMobileNumber = "09876543210",
        offenceRisk = false,
        postCodeRisk = "",
        falseLimbRisk = "",
        migratedRisk = "",
        rangeRisk = "",
        reportRisk = "",
        orderStartDate = LocalDateTime.parse("2012-02-01T00:00:00"),
        orderEndDate = LocalDateTime.parse("2013-04-03T00:00:00"),
        orderType = "Community",
        orderTypeDescription = "",
        orderTypeDetail = "",
        wearingWristPid = "",
        notifyingOrganisationDetailsName = "",
        responsibleOrganisation = "",
        responsibleOrganisationDetailsRegion = "",
      )

      Mockito.`when`(integrityOrderDetailsService.getOrderDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)).thenReturn(expectedResult)

      val result = controller.getGeneralOrderDetails(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isNotNull
      Assertions.assertThat(result.body).isInstanceOf(IntegrityOrderDetails::class.java)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)
      Mockito.verify(integrityOrderDetailsService, Mockito.times(1)).getOrderDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)
    }
  }

  @Nested
  inner class GetIntegrityRestrictedOrderDetails {
    @Test
    fun `gets restricted order details from order details service`() {
      val legacySubjectId = "2gt"
      val expectedResult = IntegrityOrderDetails(
        specials = "yes",
        legacySubjectId = "AA12345",
        legacyOrderId = "1234567",
        firstName = "John",
        lastName = "Smith",
        alias = "Zeno",
        dateOfBirth = LocalDateTime.parse("1980-02-01T00:00:00"),
        adultOrChild = "adult",
        sex = "Sex",
        contact = "contact",
        primaryAddressLine1 = "1 Primary Street",
        primaryAddressLine2 = "Sutton",
        primaryAddressLine3 = "London",
        primaryAddressPostCode = "ABC 123",
        phoneOrMobileNumber = "09876543210",
        offenceRisk = false,
        postCodeRisk = "",
        falseLimbRisk = "",
        migratedRisk = "",
        rangeRisk = "",
        reportRisk = "",
        orderStartDate = LocalDateTime.parse("2012-02-01T00:00:00"),
        orderEndDate = LocalDateTime.parse("2013-04-03T00:00:00"),
        orderType = "Community",
        orderTypeDescription = "",
        orderTypeDetail = "",
        wearingWristPid = "",
        notifyingOrganisationDetailsName = "",
        responsibleOrganisation = "",
        responsibleOrganisationDetailsRegion = "",
      )

      Mockito.`when`(integrityOrderDetailsService.getOrderDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_RESTRICTED__RO)).thenReturn(expectedResult)

      val result = controller.getRestrictedOrderDetails(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isNotNull
      Assertions.assertThat(result.body).isInstanceOf(IntegrityOrderDetails::class.java)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)
      Mockito.verify(integrityOrderDetailsService, Mockito.times(1)).getOrderDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_RESTRICTED__RO)
    }
  }
}
