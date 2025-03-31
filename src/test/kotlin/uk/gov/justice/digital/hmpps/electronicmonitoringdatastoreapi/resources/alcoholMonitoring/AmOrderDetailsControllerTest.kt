package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources.alcoholMonitoring

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.alcoholMonitoring.AmOrderDetailsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring.AmOrderDetailsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class AmOrderDetailsControllerTest {
  private lateinit var amOrderDetailsService: AmOrderDetailsService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var amOrderDetailsController: AmOrderDetailsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    amOrderDetailsService = Mockito.mock(AmOrderDetailsService::class.java)
    roleService = Mockito.mock(AthenaRoleService::class.java)
    Mockito.`when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    auditService = Mockito.mock(AuditService::class.java)
    amOrderDetailsController = AmOrderDetailsController(amOrderDetailsService, roleService, auditService)
  }

  @Nested
  inner class GetAmOrderDetails {
    @Test
    fun `gets order details from order details service`() {
      val legacySubjectId = "2gt"
      val expectedResult = AmOrderDetails(
        legacySubjectId = "AA12345",
        firstName = "John",
        lastName = "Smith",
        alias = "Zeno",
        dateOfBirth = LocalDateTime.parse("1980-02-01T00:00:00"),
        sex = "Sex",
        specialInstructions = "Special instructions",
        phoneNumber = "09876543210",
        address1 = "1 Primary Street",
        address2 = "Sutton",
        address3 = "London",
        postcode = "ABC 123",
        legacyOrderId = "1234567",
        orderStartDate = LocalDateTime.parse("2012-02-01T00:00:00"),
        orderEndDate = LocalDateTime.parse("2013-04-03T00:00:00"),
        enforceableCondition = "Enforceable condition",
        orderType = "Community",
        orderTypeDescription = "",
        orderEndOutcome = "",
        responsibleOrganisationPhoneNumber = "01234567890",
        responsibleOrganisationEmail = "a@b.c",
        tagAtSource = "",
      )

      Mockito.`when`(amOrderDetailsService.getOrderDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = amOrderDetailsController.getOrderDetails(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isNotNull
      Assertions.assertThat(result.body).isInstanceOf(AmOrderDetails::class.java)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)
      Mockito.verify(amOrderDetailsService, Mockito.times(1)).getOrderDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }
  }
}
