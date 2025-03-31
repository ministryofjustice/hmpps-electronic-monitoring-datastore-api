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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.alcoholMonitoring.AmOrderInformationController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring.AmOrderInformationService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class AmOrderInformationControllerTest {
  private lateinit var amOrderInformationService: AmOrderInformationService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var amOrderInformationController: AmOrderInformationController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    amOrderInformationService = Mockito.mock(AmOrderInformationService::class.java)
    roleService = Mockito.mock(AthenaRoleService::class.java)
    Mockito.`when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    auditService = Mockito.mock(AuditService::class.java)
    amOrderInformationController = AmOrderInformationController(amOrderInformationService, roleService, auditService)
  }

  @Nested
  inner class GetAmOrderInformation {
    @Test
    fun `gets order information from order information service`() {
      val legacySubjectId = "1ab"
      val expectedResult = AmOrderInformation(
        legacySubjectId = "AA85321",
        legacyOrderId = "1235813",
        firstName = "John",
        lastName = "Smith",
        alias = "Zeno",
        dateOfBirth = LocalDateTime.parse("1980-02-01T00:00:00"),
        address1 = "1 Primary Street",
        address2 = "Sutton",
        address3 = "London",
        postcode = "ABC 123",
        orderStartDate = LocalDateTime.parse("2012-02-01T00:00:00"),
        orderEndDate = LocalDateTime.parse("2013-04-03T00:00:00"),
      )

      Mockito.`when`(amOrderInformationService.getOrderInformation(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = amOrderInformationController.getOrderInformation(authentication, legacySubjectId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isNotNull
      Assertions.assertThat(result.body).isInstanceOf(AmOrderInformation::class.java)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)
      Mockito.verify(amOrderInformationService, Mockito.times(1)).getOrderInformation(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }
  }
}
